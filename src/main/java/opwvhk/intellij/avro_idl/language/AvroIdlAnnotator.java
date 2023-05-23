package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInspection.util.IntentionName;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiUtilCore;
import opwvhk.intellij.avro_idl.inspections.SimpleAvroIdlQuickFixOnPsiElement;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.intellij.lang.annotation.HighlightSeverity.ERROR;
import static java.util.regex.Pattern.UNICODE_CHARACTER_CLASS;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.NULL;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.VOID;

public class AvroIdlAnnotator implements Annotator, DumbAware {
	private static final String SIMPLE_NAME_IN_STRING = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
	private static final String IDENTIFIER_IN_STRING = SIMPLE_NAME_IN_STRING + "(\\." + SIMPLE_NAME_IN_STRING + ")*";
	private static final String SIMPLE_NAME = "(`" + SIMPLE_NAME_IN_STRING + "`|" + SIMPLE_NAME_IN_STRING + ")";
	private static final String IDENTIFIER = SIMPLE_NAME + "(\\." + SIMPLE_NAME + ")*";
	private static final Set<String> VALID_ORDER_NAMES = Set.of("ASCENDING", "DESCENDING", "IGNORE");

	private static final Predicate<String> VALID_SIMPLE_NAME_IN_STRING = Pattern.compile(SIMPLE_NAME_IN_STRING,
			UNICODE_CHARACTER_CLASS).asMatchPredicate();
	public static final Predicate<String> VALID_IDENTIFIER_IN_STRING = Pattern.compile(IDENTIFIER_IN_STRING,
			UNICODE_CHARACTER_CLASS).asMatchPredicate();
	private static final Predicate<String> VALID_SIMPLE_NAME = Pattern.compile(SIMPLE_NAME, UNICODE_CHARACTER_CLASS)
			.asMatchPredicate();
	private static final Predicate<String> VALID_IDENTIFIER = Pattern.compile(IDENTIFIER, UNICODE_CHARACTER_CLASS)
			.asMatchPredicate();
	private static final Predicate<String> VALID_ORDER = order -> order != null &&
			VALID_ORDER_NAMES.contains(order.toUpperCase());


	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		if (element.getNode().getElementType() == AvroIdlTypes.IDENTIFIER) {
			final PsiElement parent = element.getParent();
			if (parent instanceof AvroIdlReferenceType) {
				annotateSchemaReference(element, holder, false);
			} else if (parent instanceof AvroIdlMessageAttributeThrows) {
				annotateSchemaReference(element, holder, true);
			} else if (parent instanceof AvroIdlEnumDefault) {
				annotateEnumDefault(element, holder);
			} else if (parent instanceof AvroIdlNameIdentifierOwner) {
				annotateIdentifierName(element, holder);
			}
		} else if (element instanceof AvroIdlSchemaProperty) {
			annotateSchemaProperty((AvroIdlSchemaProperty) element, holder);
		} else if (element.getNode().getElementType() == AvroIdlTypes.ONEWAY) {
			annotateOneWay(element, holder);
		}
	}

	private void annotateSchemaReference(@NotNull PsiElement element, @NotNull AnnotationHolder holder,
	                                     boolean mustBeAnError) {
		final PsiElement parent = element.getParent();
		final AvroIdlNamedSchemaReference reference = (AvroIdlNamedSchemaReference) parent.getReference();
		assert reference !=
				null; // Because we've matched on an identifier, and our parent is a ReferenceType or MessageAttributeThrows
		final PsiElement referencedElement = reference.resolve();

		final String identifier = getIdentifier(element);
		if (referencedElement == null) {
			AnnotationBuilder annotationBuilder = holder.newAnnotation(ERROR, "Unknown schema: " + identifier);
			annotationBuilder = annotationBuilder.withFix(
					new AddEmptyRecordSchemaFix(element, identifier, mustBeAnError));
			if (!mustBeAnError) {
				annotationBuilder = annotationBuilder
						.withFix(new AddEmptyEnumSchemaFix(element, identifier))
						.withFix(new AddFixedSchemaFix(element, identifier));
			}
			annotationBuilder.create();
		} else if (mustBeAnError && !reference.resolvesToError()) {
			holder.newAnnotation(ERROR, "Not an error: " + identifier).create();
		}
	}

	@NotNull
	private String getIdentifier(@NotNull PsiElement element) {
		String text = element.getText();
		return text.startsWith("`") ? text.substring(1, text.length() - 2) : text;
	}

	private void annotateEnumDefault(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		AvroIdlEnumDeclaration enumDeclaration = (AvroIdlEnumDeclaration) element.getParent().getParent();
		final AvroIdlEnumBody enumBody = enumDeclaration.getEnumBody();
		if (enumBody == null) {
			return;
		}
		final List<AvroIdlEnumConstant> enumConstants = enumBody.getEnumConstantList();
		for (AvroIdlEnumConstant enumConstant : enumConstants) {
			if (Objects.equals(enumConstant.getName(), getIdentifier(element))) {
				return;
			}
		}
		holder.newAnnotation(ERROR, "Enum default must be one of the enum constants")
				.withFix(new AddEnumSymbolFix(element)).create();
	}

	private void annotateIdentifierName(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		final String identifier = element.getText();
		final boolean isVariableName = element.getParent() instanceof AvroIdlVariableDeclarator;
		final Predicate<String> validName = isVariableName ? VALID_SIMPLE_NAME : VALID_IDENTIFIER;

		final Map<PsiElement, NameAndLink> duplicateNameElements;
		final String identifiedElement;
		if (isVariableName) {
			final PsiElement valueHolder = element.getParent().getParent().getParent();
			if (valueHolder instanceof AvroIdlRecordBody) {
				duplicateNameElements = getDuplicateNames(valueHolder,
						() -> Stream.of(((AvroIdlRecordBody) valueHolder))
								.map(AvroIdlRecordBody::getFieldDeclarationList)
								.flatMap(List::stream)
								.map(AvroIdlFieldDeclaration::getVariableDeclaratorList)
								.flatMap(List::stream)
				);
				identifiedElement = "Field";
			} else {
				duplicateNameElements = getDuplicateNames(valueHolder,
						() -> Stream.of(((AvroIdlMessageDeclaration) valueHolder))
								.map(AvroIdlMessageDeclaration::getFormalParameterList)
								.flatMap(List::stream)
								.map(AvroIdlFormalParameter::getVariableDeclarator)
								.flatMap(Stream::ofNullable));
				identifiedElement = "Message parameter";
			}
		} else if (element.getParent() instanceof AvroIdlEnumConstant) {
			AvroIdlEnumBody enumBody = (AvroIdlEnumBody) element.getParent().getParent();
			duplicateNameElements = getDuplicateNames(enumBody,
					() -> enumBody.getEnumConstantList().stream().flatMap(Stream::ofNullable));
			identifiedElement = "Enum constant";
		} else if (element.getParent() instanceof AvroIdlNamedSchemaDeclaration) {
			PsiElement valueHolder = element.getParent().getParent();
			duplicateNameElements = getDuplicateNames(valueHolder,
					() -> ((Stream<?>) Stream.of(valueHolder.getChildren()))
							.filter(AvroIdlNamedSchemaDeclaration.class::isInstance)
							.map(AvroIdlNamedSchemaDeclaration.class::cast)
			);
			identifiedElement = "Schema";
		} else {
			duplicateNameElements = Collections.emptyMap();
			identifiedElement = null;
		}
		NameAndLink linkToDuplicate = duplicateNameElements.get(element);
		if (linkToDuplicate != null) {
			String message = duplicateElementMessage(identifiedElement, linkToDuplicate.name);
			String tooltip = "<html>" + duplicateElementMessage(identifiedElement, linkToDuplicate.link) + "</html>";
			holder.newAnnotation(ERROR, message).range(element).tooltip(tooltip).create();
		}
		if (!validName.test(identifier)) {
			holder.newAnnotation(ERROR, invalidIdentifierMessage("", identifier)).range(element).create();
		}
	}

	private static Map<PsiElement, NameAndLink> getDuplicateNames(PsiElement contextElement,
	                                                              Supplier<Stream<PsiNameIdentifierOwner>> findNames) {
		return CachedValuesManager.getCachedValue(contextElement, () -> {
			Map<String, List<PsiElement>> elementsByName = new HashMap<>();
			findNames.get().forEach(
					nameOwner -> {
						String name = nameOwner instanceof AvroIdlNamespacedNameIdentifierOwner ?
								((AvroIdlNamespacedNameIdentifierOwner) nameOwner).getFullName() : nameOwner.getName();
						elementsByName
								.computeIfAbsent(name, ignored -> new ArrayList<>())
								.add(nameOwner.getNameIdentifier());
					}
			);
			elementsByName.values().removeIf(list -> list.size() < 2);

			Set<PsiElement> dependencies = new HashSet<>();
			dependencies.add(contextElement);
			Map<PsiElement, NameAndLink> duplicates = new HashMap<>();
			elementsByName.forEach((name, duplicatesForName) -> {
				PsiElement first = duplicatesForName.get(0);
				PsiElement second = duplicatesForName.get(1);
				dependencies.add(first);
				duplicates.put(first, new NameAndLink(name, second));
				duplicatesForName.subList(1, duplicatesForName.size()).forEach(duplicate -> {
					dependencies.add(duplicate);
					duplicates.put(duplicate, new NameAndLink(name, first));
				});
			});
			return CachedValueProvider.Result.create(duplicates, dependencies);
		});
	}

	@NotNull
	private String invalidIdentifierMessage(@NotNull String suffix, @NotNull String invalidIdentifier) {
		return "Not a valid identifier" + suffix + ": " + invalidIdentifier;
	}

	private void annotateSchemaProperty(@NotNull AvroIdlSchemaProperty element, @NotNull AnnotationHolder holder) {
		final AvroIdlJsonValue jsonValue = element.getJsonValue();
		if (jsonValue == null) {
			// Syntax error: do nothing.
			return;
		}
		// Parent is one of:
		// AvroIdlAnnotatedNameIdentifierOwner
		// AvroIdlType
		final AvroIdlWithSchemaProperties parent = (AvroIdlWithSchemaProperties) element.getParent();

		if (parent instanceof AvroIdlReferenceType) {
			holder.newAnnotation(ERROR,
							"Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fail to compile.")
					.withFix(new DeleteSchemaProperty(element, "Delete annotation from reference"))
					.create();
		}

		final String schemaPropertyName = element.getName();
		if (element instanceof AvroIdlNamespaceProperty) {
			annotateNamespaceAnnotation((AvroIdlNamespaceProperty) element, holder);
		} else if ("aliases".equals(schemaPropertyName)) {
			annotateAliasesAnnotation(parent, jsonValue, holder);
		} else if ("order".equals(schemaPropertyName)) {
			annotateOrderAnnotation(parent, jsonValue, holder);
		} else if ("logicalType".equals(schemaPropertyName)) {
			annotateLogicalTypeAnnotation(parent, jsonValue, holder);
		} else if ("precision".equals(schemaPropertyName)) {
			annotatePrecisionAnnotation(parent, jsonValue, holder);
		} else if ("scale".equals(schemaPropertyName)) {
			annotateScaleAnnotation(parent, jsonValue, holder);
		}
	}

	private void annotateNamespaceAnnotation(@NotNull AvroIdlNamespaceProperty element,
	                                         @NotNull AnnotationHolder holder) {
		final PsiElement parent = element.getParent();
		if (!(parent instanceof AvroIdlProtocolDeclaration) && !(parent instanceof AvroIdlNamedSchemaDeclaration)) {
			// This location doesn't recognize the annotation as having a special meaning; treat it as a custom annotation (and thus unchecked).
			return;
		}

		final PsiElement jsonValue = element.getJsonValue();
		assert jsonValue != null : "The caller should have verified jsonValue != null";
		final String namespace = element.getName();
		if (namespace == null) {
			holder.newAnnotation(ERROR, "@namespace annotations must contain a string").range(jsonValue).create();
		}
	}

	private void annotateAliasesAnnotation(PsiElement parent, AvroIdlJsonValue jsonValue,
	                                       @NotNull AnnotationHolder holder) {
		if (!(parent instanceof AvroIdlProtocolDeclaration) && !(parent instanceof AvroIdlNamedSchemaDeclaration) &&
				!(parent instanceof AvroIdlVariableDeclarator)) {
			// This location doesn't recognize the annotation as having a special meaning; treat it as a custom annotation (and thus unchecked).
			return;
		}

		AvroIdlJsonArray jsonArray = jsonValue.getJsonArray();
		if (jsonArray != null) {
			for (AvroIdlJsonValue jsonArrayElement : jsonArray.getJsonValueList()) {
				if (jsonArrayElement == null) {
					// Syntax error: skip
					continue;
				}
				String alias = AvroIdlUtil.getJsonString(jsonArrayElement);
				if (alias == null) {
					holder.newAnnotation(ERROR, "@aliases elements must be strings").range(jsonArrayElement).create();
				} else if (parent instanceof AvroIdlVariableDeclarator) {
					if (!VALID_SIMPLE_NAME_IN_STRING.test(alias)) {
						holder.newAnnotation(ERROR, invalidIdentifierMessage("", alias)).range(jsonArrayElement)
								.create();
					}
				} else {
					// Protocol or named schema
					if (!VALID_IDENTIFIER_IN_STRING.test(alias)) {
						holder.newAnnotation(ERROR, invalidIdentifierMessage(" (with namespace)", alias))
								.range(jsonArrayElement).create();
					}
				}
			}
		} else {
			holder.newAnnotation(ERROR, "@aliases annotations must contain an array of identifiers (strings)")
					.range(jsonValue).create();
		}
	}

	private void annotateOrderAnnotation(PsiElement parent, AvroIdlJsonValue jsonValue,
	                                     @NotNull AnnotationHolder holder) {
		if (!(parent instanceof AvroIdlVariableDeclarator)) {
			// This location doesn't recognize the annotation as having a special meaning; treat it as a custom annotation (and thus unchecked).
			return;
		}

		String order = AvroIdlUtil.getJsonString(jsonValue);
		if (!VALID_ORDER.test(order)) {
			holder.newAnnotation(ERROR,
							"@order annotation must contain one of: \"ascending\", \"descending\", \"ignore\"").range(jsonValue)
					.create();
		}
	}

	private void annotateLogicalTypeAnnotation(AvroIdlWithSchemaProperties parent, AvroIdlJsonValue jsonValue,
	                                           @NotNull AnnotationHolder holder) {
		if (!(parent instanceof AvroIdlType) && !(parent instanceof AvroIdlFixedDeclaration)) {
			// This location doesn't recognize the annotation as having a special meaning; treat it as a custom annotation (and thus unchecked).
			return;
		}

		String logicalType = AvroIdlUtil.getJsonString(jsonValue);
		if (logicalType == null) {
			holder.newAnnotation(ERROR, "@logicalType annotation must contain a string naming the logical type")
					.range(jsonValue).create();
			return;
		}

		switch (logicalType) {
			case "decimal":
				boolean isCorrectType;
				if (parent instanceof AvroIdlFixedDeclaration) {
					isCorrectType = true;
				} else {
					final IElementType primitiveType = findPrimitiveType(parent);
					isCorrectType = primitiveType == AvroIdlTypes.BYTES;
				}
				if (!isCorrectType) {
					holder.newAnnotation(ERROR,
							"The logical type 'decimal' requires the underlying type bytes or fixed").create();
				}

				AvroIdlJsonValue precisionValue = findSchemaProperty(parent, "precision");
				if (precisionValue == null) {
					holder.newAnnotation(ERROR,
									"@logicalType(\"decimal\") requires a sibling @precision annotation with a number between 1 and 2^31-1")
							.create();
				}
				break;
			case "date":
			case "time-millis":
				if (findPrimitiveType(parent) != AvroIdlTypes.INT) {
					holder.newAnnotation(ERROR,
							"The logical type '" + logicalType + "' requires the underlying type int").create();
				}
				break;
			case "time-micros":
			case "timestamp-millis":
			case "timestamp-micros":
			case "local-timestamp-millis":
			case "local-timestamp-micros":
				if (findPrimitiveType(parent) != AvroIdlTypes.LONG) {
					holder.newAnnotation(ERROR,
							"The logical type '" + logicalType + "' requires the underlying type long").create();
				}
				break;
			case "duration":
				boolean isCorrectDuration = parent instanceof AvroIdlFixedDeclaration;
				if (isCorrectDuration) {
					final PsiElement intLiteral = ((AvroIdlFixedDeclaration) parent).getIntLiteral();
					isCorrectDuration = intLiteral != null && Integer.parseInt(intLiteral.getText()) == 12;
				}
				if (!isCorrectDuration) {
					holder.newAnnotation(ERROR,
							"The logical type 'duration' requires the underlying type fixed, of 12 bytes").create();
				}
				break;
		}
	}

	@Nullable
	private IElementType findPrimitiveType(@Nullable PsiElement type) {
		if (type instanceof AvroIdlPrimitiveType || type instanceof AvroIdlResultType) {
			Optional<PsiElement> primitiveTypeNode = Optional.ofNullable(type.getLastChild());
			if (((AvroIdlType) type).isOptional()) {
				// Optional types end with a '?', and are guaranteed to have a sibling before that
				primitiveTypeNode = primitiveTypeNode.map(PsiElement::getPrevSibling);
			}
			return primitiveTypeNode.map(PsiElement::getNode).map(ASTNode::getElementType).orElse(null);
		}
		return null;
	}

	@Nullable
	private AvroIdlJsonValue findSchemaProperty(@NotNull AvroIdlWithSchemaProperties type, @NotNull String name) {
		for (AvroIdlSchemaProperty schemaProperty : type.getSchemaPropertyList()) {
			if (name.equals(schemaProperty.getName())) {
				return schemaProperty.getJsonValue();
			}
		}
		return null;
	}

	private void annotatePrecisionAnnotation(PsiElement parent, AvroIdlJsonValue jsonValue,
	                                         @NotNull AnnotationHolder holder) {
		if (!(parent instanceof AvroIdlType)) {
			return;
		}

		if (!"decimal".equals(AvroIdlUtil.getJsonString(findSchemaProperty((AvroIdlType) parent, "logicalType")))) {
			return;
		}

		Long precision = AvroIdlUtil.getJsonIntValue(jsonValue);
		if (precision == null || precision < 1 || precision > Integer.MAX_VALUE) {
			holder.newAnnotation(ERROR, "@precision must contain a number between 1 and 2^31-1").range(jsonValue)
					.create();
			return;
		}

		AvroIdlFixedDeclaration fixedDeclaration = null;
		if (parent instanceof AvroIdlFixedDeclaration) {
			fixedDeclaration = (AvroIdlFixedDeclaration) parent;
		} else if (parent instanceof AvroIdlReferenceType) {
			final PsiReference reference = parent.getReference();
			assert reference != null;
			final PsiElement referencedType = reference.resolve();
			if (referencedType instanceof AvroIdlFixedDeclaration) {
				fixedDeclaration = (AvroIdlFixedDeclaration) referencedType;
			}
		}
		if (fixedDeclaration != null) {
			final PsiElement intLiteral = fixedDeclaration.getIntLiteral();
			if (intLiteral != null) {
				long fixedSize = Long.parseLong(intLiteral.getText());
				if (fixedSize < Integer.MAX_VALUE) {
					// This calculation is a copy of the one in the Avro source code.
					long maxPrecision = Math.round(Math.floor(Math.log10(2) * (8 * (int) fixedSize - 1)));
					if (precision > maxPrecision) {
						final String referencedName = fixedDeclaration.getFullName();
						holder.newAnnotation(ERROR, String.format("%s, a fixed(%d), cannot store %d digits (max %d)",
								referencedName, fixedSize, precision, maxPrecision)).range(jsonValue).create();
					}
				}
			}
		}
	}

	private void annotateScaleAnnotation(PsiElement parent, AvroIdlJsonValue jsonValue,
	                                     @NotNull AnnotationHolder holder) {
		if (!(parent instanceof AvroIdlType)) {
			return;
		}

		if (!"decimal".equals(AvroIdlUtil.getJsonString(findSchemaProperty((AvroIdlType) parent, "logicalType")))) {
			return;
		}

		Long scale = AvroIdlUtil.getJsonIntValue(jsonValue);
		boolean isIncorrect = false;

		if (scale == null || scale < 0) {
			isIncorrect = true;
		} else {
			Long precision = AvroIdlUtil.getJsonIntValue(findSchemaProperty((AvroIdlType) parent, "precision"));
			if (precision != null && scale > precision) {
				isIncorrect = true;
			}
		}

		if (isIncorrect) {
			holder.newAnnotation(ERROR, "@scale must contain a non-negative number of at most the value of @precision")
					.range(jsonValue).create();
		}

	}

	@NotNull
	private String duplicateElementMessage(String type, String name) {
		return String.format("%s '%s' is already defined", type, name);
	}

	private void annotateOneWay(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		final PsiElement messageDeclaration = element.getParent().getParent();
		if (messageDeclaration instanceof AvroIdlMessageDeclaration) {
			final IElementType primitiveType = findPrimitiveType(
					((AvroIdlMessageDeclaration) messageDeclaration).getType());
			if (primitiveType != VOID && primitiveType != NULL) {
				holder.newAnnotation(ERROR, "Oneway messages must have a void or null return type").create();
			}
		}
	}

	/**
	 * Delete a single schema property. This subclass handles write mode itself, to disable the "fix all" option.
	 */
	private static class DeleteSchemaProperty extends SimpleAvroIdlQuickFixOnPsiElement<AvroIdlSchemaProperty> {
		public DeleteSchemaProperty(@NotNull AvroIdlSchemaProperty element, @NotNull @IntentionName String text) {
			super(element, text);
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
		                      @NotNull AvroIdlSchemaProperty element) {
			ApplicationManager.getApplication().runWriteAction(element::delete);
		}

		@Override
		public boolean startInWriteAction() {
			return false;
		}
	}

	private static class NameAndLink {
		private final String name;
		private final String link;

		public NameAndLink(String name, PsiElement element) {
			this.name = name;
			this.link = Optional.of(element)
					.map(PsiUtilCore::getVirtualFile)
					.map(VirtualFile::getPath)
					.map(FileUtil::toSystemIndependentName)
					.map(path -> path + ":" + element.getTextOffset())
					.map(link -> "<a href=\"#navigation/" + link + "\">" + name + "</a>")
					.orElse(name);
		}
	}
}
