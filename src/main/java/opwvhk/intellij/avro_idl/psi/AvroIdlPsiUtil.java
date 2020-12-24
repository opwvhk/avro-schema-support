package opwvhk.intellij.avro_idl.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
		throw new IncorrectOperationException("Not supported");
	}
}
