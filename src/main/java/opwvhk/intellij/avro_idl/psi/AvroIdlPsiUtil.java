package opwvhk.intellij.avro_idl.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.CheckUtil;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceHelper;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceHelperRegistrar;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileReference;
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
import java.util.Optional;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlPsiUtil {
	@Nullable
	public static PsiElement getNameIdentifier(@NotNull AvroIdlNamedType owner) {
		ASTNode nameNode = owner.getNode().findChildByType(IDENTIFIER);
		return nameNode != null ? nameNode.getPsi() : null;
	}

	public static int getTextOffset(@NotNull AvroIdlNamedType owner) {
		final ASTNode nameNode = owner.getNode().findChildByType(IDENTIFIER);
		final ASTNode identifierNode = (nameNode != null ? nameNode : owner.getNode());
		return identifierNode.getStartOffset();
	}

	@Nullable
	@NonNls
	public static String getName(@NotNull AvroIdlNamedType owner) {
		PsiElement id = getNameIdentifier(owner);
		return id != null ? id.getText() : null;
	}

	public static PsiElement setName(@NotNull AvroIdlNamedType owner, @NonNls @NotNull String name) throws IncorrectOperationException {
		return replaceChildIdentifier(owner, name);
	}

	public static PsiElement replaceChildIdentifier(@NotNull PsiElement owner, @NonNls @NotNull String name) throws IncorrectOperationException {
		final ASTNode identifierNode = owner.getNode().findChildByType(IDENTIFIER);
		if (identifierNode == null) {
			throw new IncorrectOperationException();
		}
		final PsiElement oldIdentifier = identifierNode.getPsi();
		final PsiElement newNameIdentifier = new AvroIdlElementFactory(owner.getProject()).createIdentifier(name);
		oldIdentifier.replace(newNameIdentifier);
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
		}

		List<AvroIdlSchemaProperty> schemaProperties = null;
		if (owner instanceof AvroIdlProtocolDeclaration) {
			schemaProperties = ((AvroIdlProtocolDeclaration) owner).getSchemaPropertyList();
		} else if (owner instanceof AvroIdlNamedSchemaDeclaration) {
			schemaProperties = ((AvroIdlNamedSchemaDeclaration) owner).getSchemaPropertyList();
		}
		if (schemaProperties != null) {
			for (AvroIdlSchemaProperty schemaProperty : schemaProperties) {
				if ("namespace".equals(schemaProperty.getName())) {
					// This may create nonsense namespaces; the AvroIdlAnnotator marks bugs that cause this
					return Optional.ofNullable(schemaProperty.getJsonValue()).map(AvroIdlUtil::getJsonString).orElse("");
				}
			}
		}
		return getNamespace(owner.getParent());
	}

	public static boolean isErrorType(@NotNull PsiElement namedSchemaDeclaration) {
		return namedSchemaDeclaration.getNode().findChildByType(AvroIdlTypes.ERROR) != null;
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

	@Nullable
	public static PsiFileReference getReference(@NotNull AvroIdlJsonStringLiteral owner) {
		if (owner.getParent() instanceof AvroIdlImportDeclaration) {
			final Optional<AvroIdlImportType> importType = Optional.ofNullable(((AvroIdlImportDeclaration) owner.getParent()).getImportType());
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

			// Copied from FileReferenceSet.create(...) to add the suitableFileTypes parameter (omitted methods use the default implementation)
			final TextRange range = ElementManipulators.getValueTextRange(owner);
			int offset = range.getStartOffset();
			String text = range.substring(owner.getText());
			for (final FileReferenceHelper helper : FileReferenceHelperRegistrar.getHelpers()) {
				text = helper.trimUrl(text);
			}
			return new FileReferenceSet(text, owner, offset, null, true, true, suitableFileTypes).getLastReference();
		} else {
			return null;
		}
	}

	@Nullable
	public static PsiFileReference getLastFileReference(@NotNull AvroIdlJsonStringLiteral owner) {
		return getReference(owner);
	}

	@NotNull
	public static ItemPresentation getPresentation(final AvroIdlNamedType element) {
		//noinspection ConstantConditions
		return new ItemPresentation() {
			@Nullable
			@Override
			public String getPresentableText() {
				return element.getName();
			}

			@Nullable
			@Override
			public String getLocationString() {
				return element.getContainingFile().getName();
			}

			@Nullable
			@Override
			public Icon getIcon(boolean unused) {
				return AvroIdlIcons.FILE;
			}
		};
	}

	@NotNull
	public static ItemPresentation getPresentation(final AvroIdlEnumConstant element) {
		//noinspection ConstantConditions
		return new ItemPresentation() {
			@Nullable
			@Override
			public String getPresentableText() {
				return ((AvroIdlEnumDeclaration) element.getParent().getParent()).getName() + "." + element.getName();
			}

			@Nullable
			@Override
			public String getLocationString() {
				return element.getContainingFile().getName();
			}

			@Nullable
			@Override
			public Icon getIcon(boolean unused) {
				return AvroIdlIcons.FILE;
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
}
