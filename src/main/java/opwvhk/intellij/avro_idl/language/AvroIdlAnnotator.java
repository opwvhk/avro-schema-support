package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.intellij.lang.annotation.HighlightSeverity.*;
import static com.intellij.lang.annotation.HighlightSeverity.ERROR;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlAnnotator implements Annotator {
	private static final String identifier = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
	private static final Predicate<String> VALID_NAMESPACE = Pattern.compile(identifier + "(\\." + identifier + ")*",
		Pattern.UNICODE_CHARACTER_CLASS).asMatchPredicate();
	private static final Set<String> VALID_ORDER_NAMES = Set.of("ASCENDING", "DESCENDING", "IGNORE");
	private static final Predicate<String> VALID_ORDER = order -> order != null && VALID_ORDER_NAMES.contains(order.toUpperCase());
	private static final Predicate<String> VALID_IDENTIFIER = Pattern.compile("`" + identifier + "`|" + identifier,
		Pattern.UNICODE_CHARACTER_CLASS).asMatchPredicate();
	public static final @NotNull TokenSet DOCUMENTATION_TOKENS = TokenSet.create(DOCUMENTATION);


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
		} else if (element instanceof AvroIdlDocumentation) {
			annotateDocumentation((AvroIdlDocumentation) element, holder);
		} else if (element instanceof AvroIdlSchemaProperty) {
			annotateSchemaProperty((AvroIdlSchemaProperty) element, holder);
		} else if (element instanceof AvroIdlFile) {
			annotateFile((AvroIdlFile) element, holder);
		} else if (element.getNode().getElementType() == AvroIdlTypes.ONEWAY) {
			annotateOneWay(element, holder);
		}
	}

	private void annotateSchemaReference(@NotNull PsiElement element, @NotNull AnnotationHolder holder, boolean mustBeAnError) {
		final PsiElement parent = element.getParent();
		final AvroIdlNamedSchemaReference reference = (AvroIdlNamedSchemaReference) parent.getReference();
		assert reference != null; // Because we've matched on an identifier and our parent is a ReferenceType, MessageAttributeThrows or EnumDefault
		final PsiElement referencedElement = reference.resolve();

		if (referencedElement == null) {
			final boolean unsupportedFeatureMayCauseErrors = !(parent instanceof AvroIdlEnumDefault) && protocolContainingElementHasUnsupportedImports(parent);
			final HighlightSeverity unknownSymbolSeverity = unsupportedFeatureMayCauseErrors ? WEAK_WARNING : ERROR;
			holder.newAnnotation(unknownSymbolSeverity, "Unknown schema: " + getIdentifier(element)).create();
		} else if (mustBeAnError && !reference.resolvesToError()) {
			holder.newAnnotation(ERROR, "Not an error: " + getIdentifier(element)).create();
		}
	}

	private boolean protocolContainingElementHasUnsupportedImports(PsiElement parent) {
		PsiElement e = parent;
		do {
			e = e.getParent();
		} while (!(e instanceof AvroIdlProtocolBody));
		final AvroIdlProtocolBody protocolBody = (AvroIdlProtocolBody) e;

		return !protocolBody.getImportDeclarationList().isEmpty();
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
		holder.newAnnotation(ERROR, "Enum default must be one of the enum constants").create();
	}

	private void annotateIdentifierName(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		final String identifier = element.getText();
		if (!VALID_IDENTIFIER.test(identifier)) {
			holder.newAnnotation(ERROR, invalidIdentifierMessage("", identifier)).range(element).create();
		}
	}

	@NotNull
	private String invalidIdentifierMessage(@NotNull String suffix, @NotNull String invalidIdentifier) {
		return "Not a valid identifier" + suffix + ": " + invalidIdentifier;
	}

	private void annotateDocumentation(@NotNull AvroIdlDocumentation element, @NotNull AnnotationHolder holder) {

		final ASTNode astNode = element.getNode();
		boolean isLastDocumentation = TreeUtil.findSibling(astNode.getTreeNext(), DOCUMENTATION) == null;

		if (isLastDocumentation) {
			final ASTNode nextSibling = element.getParent() instanceof AvroIdlType ? astNode.getTreeParent() : astNode.getTreeNext();
			final ASTNode firstVariableDeclarator = TreeUtil.findSibling(nextSibling, VARIABLE_DECLARATOR);
			isLastDocumentation = firstVariableDeclarator == null ||
				TreeUtil.findSibling(firstVariableDeclarator.getFirstChildNode(), DOCUMENTATION_TOKENS) == null;
		}

		if (!isLastDocumentation) {
			holder.newAnnotation(WARNING, "Dangling documentation comment").create();
		}
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
		final PsiElement parent = element.getParent();

		final String schemaPropertyName = element.getName();
		if ("namespace".equals(schemaPropertyName)) {
			annotateNamespaceAnnotation(parent, jsonValue, holder);
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

	private void annotateNamespaceAnnotation(PsiElement parent, AvroIdlJsonValue jsonValue, @NotNull AnnotationHolder holder) {
		if (!(parent instanceof AvroIdlProtocolDeclaration) && !(parent instanceof AvroIdlNamedSchemaDeclaration)) {
			// TODO: Make an inspection out of this.
			holder.newAnnotation(WEAK_WARNING, "A @namespace annotation has no effect here").create();
			return;
		}

		String namespace = AvroIdlUtil.getJsonString(jsonValue);
		if (namespace == null) {
			holder.newAnnotation(ERROR, "@namespace annotations must contain a string").range(jsonValue).create();
		} else if (!VALID_NAMESPACE.test(namespace)) {
			holder.newAnnotation(WARNING, "The namespace is not composed of valid identifiers").range(jsonValue).create();
		}
	}

	private void annotateAliasesAnnotation(PsiElement parent, AvroIdlJsonValue jsonValue, @NotNull AnnotationHolder holder) {
		if (!(parent instanceof AvroIdlProtocolDeclaration) && !(parent instanceof AvroIdlNamedSchemaDeclaration) &&
			!(parent instanceof AvroIdlVariableDeclarator)) {
			// TODO: Make an inspection out of this.
			holder.newAnnotation(WEAK_WARNING, "An @aliases annotation has no effect here").create();
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
					if (!VALID_IDENTIFIER.test(alias)) {
						holder.newAnnotation(ERROR, invalidIdentifierMessage("", alias)).range(jsonArrayElement).create();
					}
				} else {
					// Protocol or named schema
					if (!VALID_NAMESPACE.test(alias)) {
						holder.newAnnotation(ERROR, invalidIdentifierMessage(" (with namespace)", alias)).range(jsonArrayElement).create();
					}
				}
			}
		} else {
			holder.newAnnotation(ERROR, "@aliases annotations must contain an array of identifiers (strings)").range(jsonValue).create();
		}
	}

	private void annotateOrderAnnotation(PsiElement parent, AvroIdlJsonValue jsonValue, @NotNull AnnotationHolder holder) {
		if (!(parent instanceof AvroIdlVariableDeclarator)) {
			// TODO: Make an inspection out of this.
			holder.newAnnotation(WEAK_WARNING, "An @order annotation has no effect here").create();
			return;
		}

		String order = AvroIdlUtil.getJsonString(jsonValue);
		if (!VALID_ORDER.test(order)) {
			holder.newAnnotation(ERROR, "@order annotation must contain one of: \"ascending\", \"descending\", \"ignore\"").range(jsonValue).create();
		}
	}

	private void annotateLogicalTypeAnnotation(PsiElement parent, AvroIdlJsonValue jsonValue, @NotNull AnnotationHolder holder) {
		if (!(parent instanceof AvroIdlType)) {
			// TODO: Make an inspection out of this.
			holder.newAnnotation(WEAK_WARNING, "A @logicalType annotation has no effect here").create();
			return;
		}

		String logicalType = AvroIdlUtil.getJsonString(jsonValue);
		if (logicalType == null) {
			holder.newAnnotation(ERROR, "@logicalType annotation must contain a string naming the logical type").range(jsonValue).create();
			return;
		}
		switch (logicalType) {
			case "decimal":
				boolean isCorrectType;
				if (parent instanceof AvroIdlReferenceType) {
					final PsiReference reference = parent.getReference();
					assert reference != null;
					isCorrectType = reference.resolve() instanceof AvroIdlFixedDeclaration;
				} else if (parent instanceof AvroIdlFixedDeclaration) {
					isCorrectType = true;
				} else {
					final IElementType primitiveType = findPrimitiveType(parent);
					isCorrectType = primitiveType == AvroIdlTypes.BYTES;
				}
				if (!isCorrectType) {
					holder.newAnnotation(ERROR, "The logical type 'decimal' requires the underlying type bytes or fixed").create();
				}

				AvroIdlJsonValue precisionValue = findSchemaProperty((AvroIdlType) parent, "precision");
				if (precisionValue == null) {
					holder.newAnnotation(ERROR, "@logicalType(\"decimal\") requires a sibling @precision annotation with a number between 1 and 2^31-1")
						.create();
				}
				break;
			case "date":
			case "time-millis":
				if (findPrimitiveType(parent) != AvroIdlTypes.INT) {
					holder.newAnnotation(ERROR, "The logical type '" + logicalType + "' requires the underlying type int").create();
				}
				break;
			case "time-micros":
			case "timestamp-millis":
			case "timestamp-micros":
			case "local-timestamp-millis":
			case "local-timestamp-micros":
				if (findPrimitiveType(parent) != AvroIdlTypes.LONG) {
					holder.newAnnotation(ERROR, "The logical type '" + logicalType + "' requires the underlying type long").create();
				}
				break;
		}
	}

	@Nullable
	private IElementType findPrimitiveType(@Nullable PsiElement type) {
		return (type instanceof AvroIdlPrimitiveType || type instanceof AvroIdlResultType) ? type.getLastChild().getNode().getElementType() : null;
	}

	@Nullable
	private AvroIdlJsonValue findSchemaProperty(@NotNull AvroIdlType type, @NotNull String name) {
		for (AvroIdlSchemaProperty schemaProperty : type.getSchemaPropertyList()) {
			if (name.equals(schemaProperty.getName())) {
				return schemaProperty.getJsonValue();
			}
		}
		return null;
	}

	private void annotatePrecisionAnnotation(PsiElement parent, AvroIdlJsonValue jsonValue, @NotNull AnnotationHolder holder) {
		if (!(parent instanceof AvroIdlType)) {
			return;
		}

		if (!"decimal".equals(AvroIdlUtil.getJsonString(findSchemaProperty((AvroIdlType) parent, "logicalType")))) {
			return;
		}

		Long precision = AvroIdlUtil.getJsonIntValue(jsonValue);
		if (precision == null || precision < 1 || precision > Integer.MAX_VALUE) {
			holder.newAnnotation(ERROR, "@precision must contain a number between 1 and 2^31-1").range(jsonValue).create();
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
					// This calculation is taken from the Avro source code.
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

	private void annotateScaleAnnotation(PsiElement parent, AvroIdlJsonValue jsonValue, @NotNull AnnotationHolder holder) {
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
			holder.newAnnotation(ERROR, "@scale must contain a non-negative number of at most the value of @precision").range(jsonValue).create();
		}

	}

	private void annotateFile(AvroIdlFile element, AnnotationHolder holder) {
		final PsiManager psiManager = PsiManager.getInstance(element.getProject());

		final Map<String, List<LookupElement>> allTypesByName = new LinkedHashMap<>();
		AvroIdlUtil.findAllSchemaNamesAvailableInProtocol(element).forEach(lookupElement -> lookupElement.getAllLookupStrings()
			.forEach(name -> allTypesByName.computeIfAbsent(name, ignored -> new ArrayList<>()).add(lookupElement)));

		final Collection<AvroIdlNamedSchemaDeclaration> schemasInThisFile = PsiTreeUtil.findChildrenOfType(element, AvroIdlNamedSchemaDeclaration.class);
		for (AvroIdlNamedSchemaDeclaration schema : schemasInThisFile) {
			final String fullName = schema.getFullName();
			if (fullName == null) {
				continue;
			}
			final List<LookupElement> duplicates = allTypesByName.getOrDefault(fullName, Collections.emptyList());
			if (duplicates.size() > 1) {
				//noinspection OptionalGetWithoutIsPresent
				final LookupElement anyDuplicate = duplicates.stream()
					.filter(dup -> dup.getPsiElement() == null || !psiManager.areElementsEquivalent(dup.getPsiElement(), schema))
					.findAny().get(); // Always returns something, as we're only filtering out one element

				final PsiElement psiElement = anyDuplicate.getPsiElement();
				final Object object = anyDuplicate.getObject();
				final Optional<String> linkToDuplicate = Optional.ofNullable(psiElement)
					.map(PsiUtilCore::getVirtualFile)
					.map(VirtualFile::getPath)
					.map(FileUtil::toSystemIndependentName)
					.map(path -> path + ":" + psiElement.getTextOffset())
					.or(() -> Optional.of(object)
						.filter(o -> o instanceof VirtualFile)
						.map(o -> ((VirtualFile) o).getPath())
						.map(FileUtil::toSystemIndependentName)
						.map(path -> path + ":0"))
					.map(link -> "<a href=\"#navigation/" + link + "\">" + anyDuplicate.getLookupString() + "</a>");

				final PsiElement nameIdentifier = schema.getNameIdentifier();
				assert nameIdentifier != null; // fullName!=null, thus nameIdentifier!=null
				AnnotationBuilder annotationBuilder = holder.newAnnotation(ERROR, duplicateSchemaMessage(fullName)).range(nameIdentifier);
				if (linkToDuplicate.isPresent()) {
					annotationBuilder = annotationBuilder.tooltip("<html>" + duplicateSchemaMessage(linkToDuplicate.get()) + "</html>");
				}
				annotationBuilder.create();
			}
		}
	}

	@NotNull
	private String duplicateSchemaMessage(String schemaName) {
		return String.format("Schema '%s' is already defined", schemaName);
	}

	private void annotateOneWay(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		final PsiElement messageDeclaration = element.getParent().getParent();
		if (messageDeclaration instanceof AvroIdlMessageDeclaration) {
			final IElementType primitiveType = findPrimitiveType(((AvroIdlMessageDeclaration) messageDeclaration).getType());
			if (primitiveType != VOID && primitiveType != NULL) {
				holder.newAnnotation(ERROR, "Oneway messages must have a void or null return type").create();
			}
		}
	}
}
