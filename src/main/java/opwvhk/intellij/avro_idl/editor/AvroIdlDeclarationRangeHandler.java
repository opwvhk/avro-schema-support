package opwvhk.intellij.avro_idl.editor;

import com.intellij.codeInsight.hint.DeclarationRangeHandler;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import opwvhk.intellij.avro_idl.psi.AvroIdlImportDeclaration;
import opwvhk.intellij.avro_idl.psi.AvroIdlMessageDeclaration;
import opwvhk.intellij.avro_idl.psi.AvroIdlNameIdentifierOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlDeclarationRangeHandler implements DeclarationRangeHandler<AvroIdlNameIdentifierOwner> {

	private static final TokenSet NON_DECLARATION_TYPES = TokenSet.create(WHITE_SPACE, SCHEMA_PROPERTY, LINE_COMMENT, BLOCK_COMMENT, SEMICOLON);

	@Override
	public @Nullable TextRange getDeclarationRange(@NotNull AvroIdlNameIdentifierOwner container) {
		ASTNode firstDeclarationChild = container.getNode().getFirstChildNode();
		while (firstDeclarationChild != null && NON_DECLARATION_TYPES.contains(firstDeclarationChild.getElementType())) {
			firstDeclarationChild = firstDeclarationChild.getTreeNext();
		}
		if (firstDeclarationChild == null) {
			return null;
		}

		int endOffset;
		if (container instanceof AvroIdlMessageDeclaration || container instanceof AvroIdlImportDeclaration) {
			ASTNode lastDeclarationChild = container.getNode().getFirstChildNode();
			while (lastDeclarationChild != null && NON_DECLARATION_TYPES.contains(lastDeclarationChild.getElementType())) {
				lastDeclarationChild = lastDeclarationChild.getTreePrev();
			}
			assert lastDeclarationChild != null; // Worst case: the same as firstDeclarationChild
			endOffset = lastDeclarationChild.getTextRange().getEndOffset();
		} else {
			final PsiElement nameIdentifier = container.getNameIdentifier();
			if (nameIdentifier == null) {
				return null;
			}
			endOffset = nameIdentifier.getTextRange().getEndOffset();
		}

		int startOffset = firstDeclarationChild.getStartOffset();
		return TextRange.from(startOffset, endOffset - startOffset);
	}
}
