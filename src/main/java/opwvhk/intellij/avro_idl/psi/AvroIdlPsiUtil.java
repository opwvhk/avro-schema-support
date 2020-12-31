package opwvhk.intellij.avro_idl.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.CheckUtil;
import com.intellij.util.IncorrectOperationException;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
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
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.IDENTIFIER;

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
		final PsiElement newNameIdentifier = AvroIdlElementFactory.createIdentifier(owner.getProject(), name);
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
		if (owner.getIdentifier() == null) {
			return null;
		}
		return new AvroIdlNamedSchemaReference(owner);
	}

	@NotNull
	public static AvroIdlNamedSchemaReference getReference(@NotNull AvroIdlMessageAttributeThrows owner) {
		return new AvroIdlNamedSchemaReference(owner);
	}

	@NotNull
	public static AvroIdlEnumConstantReference getReference(@NotNull AvroIdlEnumDefault owner) {
		return new AvroIdlEnumConstantReference(owner);
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
