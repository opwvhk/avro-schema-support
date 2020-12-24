package opwvhk.intellij.avro_idl.editor;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlDeclarationBlock extends AvroIdlBlock {
	private static final TokenSet DECLARATION_PREAMBLE = TokenSet.create(DOCUMENTATION, SCHEMA_PROPERTY, LINE_COMMENT, DOC_COMMENT, WHITE_SPACE);
	private static final TokenSet DECLARATION_BODY_STARTS = TokenSet.create(LEFT_BRACE, LEFT_PAREN);
	private static final TokenSet DECLARATION_BODY_ENDS = TokenSet.create(RIGHT_BRACE, RIGHT_PAREN);

	private int headerStartIndex;
	private int bodyStartIndex;
	private int bodyEndIndex;

	protected AvroIdlDeclarationBlock(@NotNull ASTNode node, @NotNull Wrap wrap, @Nullable Alignment alignment, Indent indent,
									  @NotNull SpacingBuilder spacingBuilder) {
		super(node, wrap, alignment, indent, spacingBuilder);

		headerStartIndex = -1;
		bodyStartIndex = -1;
		bodyEndIndex = -1;
		final IElementType myElementType = myNode.getElementType();
		if (DECLARATIONS.contains(myElementType)) {
			final @NotNull ASTNode[] children = myNode.getChildren(null);
			for (int i = 0; i < children.length; i++) {
				final IElementType childElementType = children[i].getElementType();
				if (headerStartIndex == -1 && !DECLARATION_PREAMBLE.contains(childElementType)) {
					headerStartIndex = i;
				} else if (headerStartIndex != -1 && DECLARATION_BODY_STARTS.contains(childElementType)) {
					bodyStartIndex = i;
				} else if (bodyStartIndex != -1 && DECLARATION_BODY_ENDS.contains(childElementType)) {
					bodyEndIndex = i;
				}
			}
		}
	}

	@Override
	protected Indent getChildIndent(int childNodeIndex) {
		Indent childIndent;
		// For declarations without a body, like field declarations and types, this works as well because the header and tail indents are the same.
		if (childNodeIndex <= headerStartIndex) {
			childIndent = Indent.getNoneIndent();
		} else if (childNodeIndex < bodyStartIndex) {
			childIndent = Indent.getContinuationIndent();
		} else if (childNodeIndex == bodyStartIndex) {
			childIndent = Indent.getNoneIndent();
		} else if (childNodeIndex < bodyEndIndex) {
			childIndent = Indent.getNormalIndent();
		} else if (childNodeIndex == bodyEndIndex) {
			childIndent = Indent.getNoneIndent();
		} else {
			childIndent = Indent.getContinuationIndent();
		}
		return childIndent;
	}

	@Override
	protected Indent getPreviousChildIndent(int childNodeIndex) {
		Indent childIndent;
		// For declarations without a body, like field declarations and types, this works as well because the header and tail indents are the same.
		if (childNodeIndex <= headerStartIndex) {
			childIndent = Indent.getNoneIndent();
		} else if (childNodeIndex <= bodyStartIndex) {
			childIndent = Indent.getContinuationIndent();
		} else if (childNodeIndex <= bodyEndIndex) {
			childIndent = Indent.getNormalIndent();
		} else {
			childIndent = Indent.getContinuationIndent();
		}
		return childIndent;
	}
}
