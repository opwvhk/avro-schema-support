package opwvhk.intellij.avro_idl.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.CheckUtil;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileReference;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
import opwvhk.intellij.avro_idl.AvroProtocolFileType;
import opwvhk.intellij.avro_idl.AvroSchemaFileType;
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
import java.util.stream.Collectors;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlPsiUtil {
	public static @NotNull List<AvroIdlNamedSchemaDeclaration> getNamedSchemaDeclarationList(@NotNull AvroIdlProtocolBody protocolBody) {
		return filterList(protocolBody.getWithSchemaPropertiesList(), AvroIdlNamedSchemaDeclaration.class);
	}

	public static @NotNull List<AvroIdlMessageDeclaration> getMessageDeclarationList(@NotNull AvroIdlProtocolBody protocolBody) {
		return filterList(protocolBody.getWithSchemaPropertiesList(), AvroIdlMessageDeclaration.class);
	}

	public static @NotNull List<AvroIdlVariableDeclarator> getVariableDeclaratorList(@NotNull AvroIdlFieldDeclaration fieldDeclaration) {
		return filterList(fieldDeclaration.getWithSchemaPropertiesList(), AvroIdlVariableDeclarator.class);
	}

	public static @NotNull AvroIdlType getType(@NotNull AvroIdlFieldDeclaration fieldDeclaration) {
		// The AvroIdlFieldDeclaration production is pinned on the type, so this is guaranteed to exist.
		final Optional<AvroIdlType> avroIdlType = filterFirst(fieldDeclaration.getWithSchemaPropertiesList(), AvroIdlType.class);
		assert avroIdlType.isPresent();
		return avroIdlType.get();
	}

	public static @Nullable AvroIdlVariableDeclarator getVariableDeclarator(@NotNull AvroIdlFormalParameter formalParameter) {
		return filterFirst(formalParameter.getWithSchemaPropertiesList(), AvroIdlVariableDeclarator.class).orElse(null);
	}

	public static @NotNull AvroIdlType getType(@NotNull AvroIdlFormalParameter formalParameter) {
		// The AvroIdlFormalParameter production is pinned on the type, so this is guaranteed to exist.
		final Optional<AvroIdlType> avroIdlType = filterFirst(formalParameter.getWithSchemaPropertiesList(), AvroIdlType.class);
		assert avroIdlType.isPresent();
		return avroIdlType.get();
	}

	private static <T> @NotNull List<T> filterList(@NotNull List<? super T> list, Class<T> clazz) {
		return list.stream().filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toList());
	}

	private static <T> @NotNull Optional<T> filterFirst(@NotNull List<? super T> list, Class<T> clazz) {
		return list.stream().filter(clazz::isInstance).map(clazz::cast).findFirst();
	}

	public static @Nullable PsiElement getNameIdentifier(@NotNull AvroIdlNamedType owner) {
		if (owner instanceof AvroIdlNamespaceProperty) {
			final AvroIdlJsonValue jsonValue = ((AvroIdlNamespaceProperty)owner).getJsonValue();
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

	public static @Nullable @NonNls String getName(@NotNull AvroIdlNamedType owner) {
		PsiElement nameIdentifier = getNameIdentifier(owner);
		if (nameIdentifier instanceof AvroIdlJsonStringLiteral) {
			return AvroIdlUtil.getJsonString((AvroIdlJsonStringLiteral)nameIdentifier);
		} else if (nameIdentifier != null) {
			return nameIdentifier.getText();
		} else {
			return null;
		}
	}

	public static PsiElement setName(@NotNull AvroIdlNamedType owner, @NonNls @NotNull String name) throws IncorrectOperationException {
		final PsiElement oldIdentifier = getNameIdentifier(owner);
		final PsiElement newNameIdentifier;
		if (oldIdentifier instanceof AvroIdlJsonStringLiteral) {
			newNameIdentifier = new AvroIdlElementFactory(owner.getProject()).createJsonStringLiteral(name);
		} else if (oldIdentifier != null) {
			newNameIdentifier = new AvroIdlElementFactory(owner.getProject()).createIdentifier(name);
		} else {
			throw new IncorrectOperationException();
		}
		oldIdentifier.replace(newNameIdentifier);
		return owner;
	}

	public static @Nullable @NonNls String getFullName(@NotNull AvroIdlNameIdentifierOwner owner) {
		String name = getName(owner);
		if (name == null || name.contains(".")) {
			return name;
		}
		return getNamespacePrefix(owner) + name;
	}

	public static @NotNull String getNamespacePrefix(@Nullable PsiElement owner) {
		String namespace = getNamespace(owner);
		return namespace.isEmpty() ? "" : namespace + ".";
	}

	public static @NotNull String getNamespace(@Nullable PsiElement owner) {
		if (owner == null) {
			return "";
		}

		List<AvroIdlSchemaProperty> schemaProperties = null;
		if (owner instanceof AvroIdlProtocolDeclaration) {
			schemaProperties = ((AvroIdlProtocolDeclaration)owner).getSchemaPropertyList();
		} else if (owner instanceof AvroIdlNamedSchemaDeclaration) {
			schemaProperties = ((AvroIdlNamedSchemaDeclaration)owner).getSchemaPropertyList();
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
		return owner instanceof AvroIdlNullableType && TreeUtil.findChildBackward(owner.getNode(), QUESTION_MARK) != null;
	}

	public static boolean isNull(@NotNull AvroIdlType owner) {
		return owner instanceof AvroIdlPrimitiveType && TreeUtil.findChildBackward(owner.getNode(), NULL) != null;
	}

	public static @Nullable AvroIdlNamedSchemaReference getReference(@NotNull AvroIdlReferenceType owner) {
		return AvroIdlNamedSchemaReference.forType(owner);
	}

	public static @NotNull AvroIdlNamedSchemaReference getReference(@NotNull AvroIdlMessageAttributeThrows owner) {
		return AvroIdlNamedSchemaReference.forMessageAttribute(owner);
	}

	public static @NotNull AvroIdlEnumConstantReference getReference(@NotNull AvroIdlEnumDefault owner) {
		return AvroIdlEnumConstantReference.forDefault(owner);
	}

	public static @Nullable PsiFileReference getReference(@NotNull AvroIdlJsonStringLiteral owner) {
		if (owner.getParent() instanceof AvroIdlImportDeclaration) {
			final Optional<AvroIdlImportType> importType = Optional.ofNullable(((AvroIdlImportDeclaration)owner.getParent()).getImportType());
			final IElementType importElementType = importType.map(PsiElement::getFirstChild).map(PsiElement::getNode).map(ASTNode::getElementType).orElse(null);

			final FileType[] suitableFileTypes;
			if (importElementType == IDL) {
				suitableFileTypes = new FileType[]{AvroIdlFileType.INSTANCE};
			} else if (importElementType == PROTOCOL) {
				suitableFileTypes = new FileType[]{AvroProtocolFileType.INSTANCE};
			} else if (importElementType == SCHEMA) {
				suitableFileTypes = new FileType[]{AvroSchemaFileType.INSTANCE};
			} else {
				suitableFileTypes = new FileType[]{AvroIdlFileType.INSTANCE, AvroProtocolFileType.INSTANCE, AvroSchemaFileType.INSTANCE};
			}

			// Copied from FileReferenceSet(PsiElement) to add the suitableFileTypes parameter
			TextRange range = ElementManipulators.getValueTextRange(owner);
			int offset = range.getStartOffset();
			String text = range.substring(owner.getText());
			final FileReferenceSet fileReferenceSet = new FileReferenceSet(text, owner, offset, null, true, true, suitableFileTypes);

			final FileReference[] allReferences = fileReferenceSet.getAllReferences();
			return allReferences.length == 0 ? null : allReferences[0];
		} else {
			return null;
		}
	}

	public static @Nullable PsiFileReference getLastFileReference(@NotNull AvroIdlJsonStringLiteral owner) {
		return getReference(owner);
	}

	public static @NotNull ItemPresentation getPresentation(final AvroIdlNamedType element) {
		//noinspection ConstantConditions
		return new ItemPresentation() {
			@Override
			public @Nullable String getPresentableText() {
				return element.getName();
			}

			@Override
			public @Nullable String getLocationString() {
				return element.getContainingFile().getName();
			}

			@Override
			public @Nullable Icon getIcon(boolean unused) {
				return AvroIdlIcons.LOGO;
			}
		};
	}

	public static @NotNull ItemPresentation getPresentation(final AvroIdlEnumConstant element) {
		//noinspection ConstantConditions
		return new ItemPresentation() {
			@Override
			public @Nullable String getPresentableText() {
				return ((AvroIdlEnumDeclaration)element.getParent().getParent()).getName() + "." + element.getName();
			}

			@Override
			public @Nullable String getLocationString() {
				return element.getContainingFile().getName();
			}

			@Override
			public @Nullable Icon getIcon(boolean unused) {
				return AvroIdlIcons.LOGO;
			}
		};
	}

	public static @Nullable PsiElement getOwner(@NotNull AvroIdlDocumentation documentation) {
		return documentation.getParent();
	}

	public static @NotNull IElementType getTokenType(@NotNull AvroIdlDocumentation documentation) {
		return documentation.getFirstChild().getNode().getElementType();
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
}
