package opwvhk.intellij.avro_idl.syntax;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;

import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlTokenSets {
	public static final TokenSet COMMENTS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT, BLOCK_COMMENT_START, INCOMPLETE_BLOCK_COMMENT, DOC_COMMENT,
		INCOMPLETE_DOC_COMMENT);
	public static final TokenSet WHITE_SPACE = TokenSet.create(TokenType.WHITE_SPACE);
	public static final TokenSet STRING_LITERALS = TokenSet.create(STRING_LITERAL);
}
