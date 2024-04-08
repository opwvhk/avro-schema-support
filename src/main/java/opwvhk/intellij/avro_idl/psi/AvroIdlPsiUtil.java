package opwvhk.intellij.avro_idl.psi;

import com.intellij.application.options.CodeStyle;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.CheckUtil;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileReference;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
import opwvhk.intellij.avro_idl.AvroProtocolFileType;
import opwvhk.intellij.avro_idl.AvroSchemaFileType;
import opwvhk.intellij.avro_idl.editor.AvroIdlCodeStyleSettings;
import opwvhk.intellij.avro_idl.language.AvroIdlEnumConstantReference;
import opwvhk.intellij.avro_idl.language.AvroIdlNamedSchemaReference;
import opwvhk.intellij.avro_idl.language.AvroIdlUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlPsiUtil {
	@NotNull
	public static List<AvroIdlNamedSchemaDeclaration> getNamedSchemaDeclarationList(
			@NotNull AvroIdlProtocolBody protocolBody) {
		return filterList(protocolBody.getWithSchemaPropertiesList(), AvroIdlNamedSchemaDeclaration.class);
	}

	@NotNull
	public static List<AvroIdlMessageDeclaration> getMessageDeclarationList(@NotNull AvroIdlProtocolBody protocolBody) {
		return filterList(protocolBody.getWithSchemaPropertiesList(), AvroIdlMessageDeclaration.class);
	}

	@NotNull
	public static List<AvroIdlVariableDeclarator> getVariableDeclaratorList(
			@NotNull AvroIdlFieldDeclaration fieldDeclaration) {
		return filterList(fieldDeclaration.getWithSchemaPropertiesList(), AvroIdlVariableDeclarator.class);
	}

	@NotNull
	public static AvroIdlType getType(@NotNull AvroIdlFieldDeclaration fieldDeclaration) {
		// The AvroIdlFieldDeclaration production pins on the type, so this is guaranteed to exist.
		final Optional<AvroIdlType> avroIdlType = filterFirst(fieldDeclaration.getWithSchemaPropertiesList(),
				AvroIdlType.class);
		assert avroIdlType.isPresent();
		return avroIdlType.get();
	}

	@Nullable
	public static AvroIdlVariableDeclarator getVariableDeclarator(@NotNull AvroIdlFormalParameter formalParameter) {
		return filterFirst(formalParameter.getWithSchemaPropertiesList(), AvroIdlVariableDeclarator.class).orElse(null);
	}

	@NotNull
	public static AvroIdlType getType(@NotNull AvroIdlFormalParameter formalParameter) {
		// The AvroIdlFormalParameter production pins on the type, so this is guaranteed to exist.
		final Optional<AvroIdlType> avroIdlType = filterFirst(formalParameter.getWithSchemaPropertiesList(),
				AvroIdlType.class);
		assert avroIdlType.isPresent();
		return avroIdlType.get();
	}

	private static <T> @NotNull List<T> filterList(@NotNull List<? super T> list, Class<T> clazz) {
		return list.stream().filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toList());
	}

	private static <T> @NotNull Optional<T> filterFirst(@NotNull List<? super T> list, Class<T> clazz) {
		return list.stream().filter(clazz::isInstance).map(clazz::cast).findFirst();
	}

	@Nullable
	public static PsiElement getNameIdentifier(@NotNull AvroIdlNamedType owner) {
		if (owner instanceof AvroIdlNamespaceProperty) {
			final AvroIdlJsonValue jsonValue = ((AvroIdlNamespaceProperty) owner).getJsonValue();
			return jsonValue instanceof AvroIdlJsonStringLiteral ? jsonValue : null;
		} else {
			ASTNode nameNode = owner.getNode().findChildByType(IDENTIFIER);
			return nameNode != null ? nameNode.getPsi() : null;
		}
	}

	public static int getTextOffset(@NotNull AvroIdlNamedType owner) {
		final PsiElement nameIdentifier = getNameIdentifier(owner);
		if (nameIdentifier instanceof AvroIdlJsonStringLiteral) {
			final TextRange range = ElementManipulators.getValueTextRange(nameIdentifier);
			return nameIdentifier.getStartOffsetInParent() + range.getStartOffset();
		} else {
			return Objects.requireNonNullElse(nameIdentifier, owner).getNode().getStartOffset();
		}
	}

	@Nullable
	@NonNls
	public static String getName(@NotNull AvroIdlNamedType owner) {
		PsiElement nameIdentifier = getNameIdentifier(owner);
		return nameFromIdentifier(nameIdentifier);
	}

	@Nullable
	private static String nameFromIdentifier(PsiElement nameIdentifier) {
		if (nameIdentifier instanceof AvroIdlJsonStringLiteral) {
			return AvroIdlUtil.getJsonString((AvroIdlJsonStringLiteral) nameIdentifier);
		} else if (nameIdentifier != null) {
			return nameIdentifier.getText();
		} else {
			return null;
		}
	}

	public static PsiElement setName(@NotNull AvroIdlNamedType owner, @NonNls @NotNull String name)
			throws IncorrectOperationException {
		final AvroIdlElementFactory elementFactory = new AvroIdlElementFactory(owner.getProject());

		final PsiElement oldIdentifier = getNameIdentifier(owner);
		final String oldName = nameFromIdentifier(oldIdentifier);
		final PsiElement newNameIdentifier;
		if (oldIdentifier instanceof AvroIdlJsonStringLiteral) {
			newNameIdentifier = elementFactory.createJsonStringLiteral(name);
		} else if (oldIdentifier != null) {
			newNameIdentifier = elementFactory.createIdentifier(name);
		} else {
			throw new IncorrectOperationException();
		}
		oldIdentifier.replace(newNameIdentifier);

		if (owner instanceof AvroIdlNamedSchemaDeclaration || owner instanceof AvroIdlVariableDeclarator) {
			AvroIdlCodeStyleSettings avroIdlCustomSettings = CodeStyle.getCustomSettings(owner.getContainingFile(),
					AvroIdlCodeStyleSettings.class);
			boolean shouldAddAlias = owner instanceof AvroIdlNamedSchemaDeclaration ?
					avroIdlCustomSettings.ADD_ALIAS_ON_SCHEMA_RENAME :
					avroIdlCustomSettings.ADD_ALIAS_ON_FIELD_RENAME;
			if (shouldAddAlias) {
				AvroIdlAnnotatedNameIdentifierOwner annotatedOwner = (AvroIdlAnnotatedNameIdentifierOwner) owner;
				AvroIdlSchemaProperty existingAliases = annotatedOwner.getSchemaPropertyList().stream()
						.filter(p -> "aliases".equals(p.getName()))
						.findFirst().orElse(null);
				if (existingAliases != null) {
					// The element already has aliases
					String text = Optional.ofNullable(existingAliases.getJsonValue())
							.map(PsiElement::getText)
							.map(String::stripLeading)
							.orElse(null);
					if (text != null && text.startsWith("[")) {
						// Only add alias if current aliases are valid
						String newText = String.format("[\"%s\", %s", oldName, text.substring(1));
						AvroIdlSchemaProperty newAliases = elementFactory.createProperty("aliases", newText);
						existingAliases.replace(newAliases);
					}
				} else {
					// The element already has no aliases yet
					AvroIdlSchemaProperty aliasesProperty = elementFactory.createProperty("aliases",
							String.format("[\"%s\"]", oldName));
					annotatedOwner.addBefore(aliasesProperty, annotatedOwner.getFirstChild());
				}
			}
		}

		return owner;
	}

	@Nullable
	@NonNls
	public static String getFullName(@NotNull AvroIdlNameIdentifierOwner owner) {
		String name = getName(owner);
		if (name == null || name.contains(".")) {
			return name;
		}
		return getNamespacePrefix(owner) + name;
	}

	@NotNull
	public static String getNamespacePrefix(@Nullable PsiElement owner) {
		String namespace = getNamespace(owner);
		return namespace.isEmpty() ? "" : namespace + ".";
	}

	@NotNull
	public static String getNamespace(@Nullable PsiElement owner) {
		if (owner == null) {
			return "";
		} else if (owner instanceof AvroIdlFile) {
			return Stream.of(owner.getChildren())
					.filter(AvroIdlNamespaceDeclaration.class::isInstance)
					.map(AvroIdlNamespaceDeclaration.class::cast)
					.findFirst()
					.map(AvroIdlNamespaceDeclaration::getName)
					.orElse("");
		}

		List<AvroIdlSchemaProperty> schemaProperties = null;
		if (owner instanceof AvroIdlProtocolDeclaration) {
			schemaProperties = ((AvroIdlProtocolDeclaration) owner).getSchemaPropertyList();
		} else if (owner instanceof AvroIdlNamedSchemaDeclaration) {
			schemaProperties = ((AvroIdlNamedSchemaDeclaration) owner).getSchemaPropertyList();
		}
		if (schemaProperties != null) {
			for (AvroIdlSchemaProperty schemaProperty : schemaProperties) {
				if (schemaProperty instanceof AvroIdlNamespaceProperty) {
					// This may create nonsense namespaces; the AvroIdlAnnotator marks bugs that cause this
					return Optional.ofNullable(schemaProperty.getName()).orElse("");
				}
			}
		}
		return getNamespace(owner.getParent());
	}

	public static boolean isErrorType(@NotNull PsiElement namedSchemaDeclaration) {
		return namedSchemaDeclaration.getNode().findChildByType(AvroIdlTypes.ERROR) != null;
	}

	public static boolean isOptional(@NotNull AvroIdlType owner) {
		return owner instanceof AvroIdlNullableType &&
				TreeUtil.findChildBackward(owner.getNode(), QUESTION_MARK) != null;
	}

	public static boolean isNull(@NotNull AvroIdlType owner) {
		return owner instanceof AvroIdlPrimitiveType && TreeUtil.findChildBackward(owner.getNode(), NULL) != null;
	}

	@Nullable
	public static AvroIdlNamedSchemaReference getReference(@NotNull AvroIdlReferenceType owner) {
		return AvroIdlNamedSchemaReference.forType(owner);
	}

	@NotNull
	public static AvroIdlNamedSchemaReference getReference(@NotNull AvroIdlMessageAttributeThrows owner) {
		return AvroIdlNamedSchemaReference.forMessageAttribute(owner);
	}

	@NotNull
	public static AvroIdlEnumConstantReference getReference(@NotNull AvroIdlEnumDefault owner) {
		return AvroIdlEnumConstantReference.forDefault(owner);
	}

	@NotNull
	public static PsiReference[] getReferences(@NotNull AvroIdlJsonStringLiteral owner) {
		PsiFileReference ref = getReference(owner);
		return ref != null ? new PsiReference[]{ref} : ReferenceProvidersRegistry.getReferencesFromProviders(owner);
	}

	@Nullable
	public static PsiFileReference getReference(@NotNull AvroIdlJsonStringLiteral owner) {
		if (owner.getParent() instanceof AvroIdlImportDeclaration) {
			final Optional<AvroIdlImportType> importType = Optional.ofNullable(
					((AvroIdlImportDeclaration) owner.getParent()).getImportType());
			final IElementType importElementType = importType.map(PsiElement::getFirstChild).map(PsiElement::getNode)
					.map(ASTNode::getElementType).orElse(null);

			final FileType[] suitableFileTypes = getSuitableFileTypes(importElementType);

			// Copied from FileReferenceSet(PsiElement) to add the suitableFileTypes parameter
			TextRange range = ElementManipulators.getValueTextRange(owner);
			int offset = range.getStartOffset();
			String text = range.substring(owner.getText());
			final FileReferenceSet fileReferenceSet = new FileReferenceSet(text, owner, offset, null, true, true,
					suitableFileTypes);

			final FileReference[] allReferences = fileReferenceSet.getAllReferences();
			return allReferences.length == 0 ? null : allReferences[0];
		} else {
			return null;
		}
	}

	private static FileType @NotNull [] getSuitableFileTypes(IElementType importElementType) {
		final FileType[] suitableFileTypes;
		if (importElementType == IDL) {
			suitableFileTypes = new FileType[]{AvroIdlFileType.INSTANCE};
		} else if (importElementType == PROTOCOL) {
			suitableFileTypes = new FileType[]{AvroProtocolFileType.INSTANCE};
		} else if (importElementType == SCHEMA) {
			suitableFileTypes = new FileType[]{AvroSchemaFileType.INSTANCE};
		} else {
			suitableFileTypes = new FileType[]{AvroIdlFileType.INSTANCE, AvroProtocolFileType.INSTANCE,
					AvroSchemaFileType.INSTANCE};
		}
		return suitableFileTypes;
	}

	@Nullable
	public static PsiFileReference getLastFileReference(@NotNull AvroIdlJsonStringLiteral owner) {
		return getReference(owner);
	}

	@Nullable
	public static Object getValue(@NotNull AvroIdlJsonStringLiteral owner) {
		return AvroIdlUtil.getJsonString(owner);
	}

	@NotNull
	public static List<AvroIdlEnumConstant> getComponents(@NotNull AvroIdlEnumBody enumBody) {
		return enumBody.getEnumConstantList();
	}

	@NotNull
	public static List<AvroIdlVariableDeclarator> getComponents(@NotNull AvroIdlFieldDeclaration fieldDeclaration) {
		return fieldDeclaration.getVariableDeclaratorList();
	}

	@NotNull
	public static List<AvroIdlFormalParameter> getComponents(@NotNull AvroIdlMessageDeclaration messageDeclaration) {
		return messageDeclaration.getFormalParameterList();
	}

	@NotNull
	public static List<AvroIdlType> getComponents(@NotNull AvroIdlUnionType unionType) {
		return unionType.getTypeList();
	}

	@NotNull
	public static List<AvroIdlJsonPair> getComponents(@NotNull AvroIdlJsonObject jsonObject) {
		return jsonObject.getJsonPairList();
	}

	@NotNull
	public static List<AvroIdlJsonValue> getComponents(@NotNull AvroIdlJsonArray jsonArray) {
		return jsonArray.getJsonValueList();
	}

	@NotNull
	public static ItemPresentation getPresentation(final AvroIdlNamedType element) {
		return new ItemPresentation() {
			@Override
			@Nullable
			public String getPresentableText() {
				return element.getName();
			}

			@Override
			public @NotNull String getLocationString() {
				return element.getContainingFile().getName();
			}

			@Override
			public @NotNull Icon getIcon(boolean unused) {
				return AvroIdlIcons.AVDL_FILE;
			}
		};
	}

	@NotNull
	public static ItemPresentation getPresentation(final AvroIdlEnumConstant element) {
		return new ItemPresentation() {
			@Override
			public @NotNull String getPresentableText() {
				String parentName = ((AvroIdlEnumDeclaration) element.getParent().getParent()).getName();
				return element.getName() + " in " + parentName;
			}

			@Override
			public @NotNull String getLocationString() {
				return element.getContainingFile().getName();
			}

			@Override
			public @NotNull Icon getIcon(boolean unused) {
				return AvroIdlIcons.AVRO_LOGO;
			}
		};
	}

	@NotNull
	public static ItemPresentation getPresentation(final AvroIdlVariableDeclarator element) {
		return new ItemPresentation() {
			@Override
			public @NotNull String getPresentableText() {
				PsiElement typeOwner = element.getParent();
				String parentName;
				if (typeOwner instanceof AvroIdlFormalParameter) {
					parentName = ((AvroIdlMessageDeclaration) typeOwner.getParent()).getName();
				} else {
					parentName = ((AvroIdlRecordDeclaration) typeOwner.getParent().getParent()).getName();
				}
				return element.getName() + " in " + parentName;
			}

			@Override
			public @NotNull String getLocationString() {
				return element.getContainingFile().getName();
			}

			@Override
			public @NotNull Icon getIcon(boolean unused) {
				return AvroIdlIcons.AVRO_LOGO;
			}
		};
	}

	public static void delete(@NotNull AvroIdlNamedSchemaDeclaration owner) throws IncorrectOperationException {
		CheckUtil.checkWritable(owner);

		// Named types are never the first/last elements in the tree
		final ASTNode parentNode = owner.getParent().getNode();
		ASTNode node = owner.getNode();
		ASTNode prev = node.getTreePrev();
		ASTNode next = node.getTreeNext();

		parentNode.removeChild(node);
		if ((prev == null || prev.getElementType() == WHITE_SPACE) && next != null &&
				next.getElementType() == WHITE_SPACE) {
			parentNode.removeChild(next);
		}
	}

	/**
	 * Return the next code leaf, or the next documentation comment leaf, whichever comes first.
	 */
	@Nullable
	public static PsiElement nextNonCommentLeaf(@Nullable PsiElement element) {
		return skipMatching(element, PsiTreeUtil::nextLeaf, AvroIdlPsiUtil::isWhitespaceOrNonDocComment, true);
	}

	/**
	 * Return the previous code leaf, or the previous documentation comment leaf, whichever comes first.
	 */
	@Nullable
	public static PsiElement prevNonCommentLeaf(@Nullable PsiElement element) {
		return skipMatching(element, PsiTreeUtil::prevLeaf, AvroIdlPsiUtil::isWhitespaceOrNonDocComment, true);
	}

	@Nullable
	public static PsiElement skipMatching(@Nullable PsiElement element,
	                                      @NotNull Function<? super PsiElement, ? extends PsiElement> next,
	                                      @NotNull Predicate<? super PsiElement> condition, boolean strict) {
		if (element == null) {
			return null;
		}
		for (PsiElement e = strict ? next.apply(element) : element; e != null; e = next.apply(e)) {
			if (!condition.test(e)) {
				return e;
			}
		}
		return null;
	}

	public static boolean isWhitespaceOrNonDocComment(PsiElement element) {
		if (element == null) {
			return false;
		} else if (element instanceof PsiComment) {
			return ((PsiComment) element).getTokenType() != DOC_COMMENT;
		} else {
			return element instanceof PsiWhiteSpace;
		}
	}

	public static boolean isDocComment(PsiElement element) {
		return element instanceof PsiComment && ((PsiComment) element).getTokenType() == DOC_COMMENT;
	}
}
