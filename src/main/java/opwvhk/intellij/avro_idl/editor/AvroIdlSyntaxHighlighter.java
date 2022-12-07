package opwvhk.intellij.avro_idl.editor;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import opwvhk.intellij.avro_idl.psi.AvroIdlTypes;
import opwvhk.intellij.avro_idl.syntax.AvroIdlLexer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.intellij.openapi.editor.colors.TextAttributesKey.EMPTY_ARRAY;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.BLOCK_COMMENT;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.DOC_COMMENT;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.STRING;
import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;
import static opwvhk.intellij.avro_idl.editor.AvroIdlSyntaxColors.*;

public class AvroIdlSyntaxHighlighter extends SyntaxHighlighterBase {
	private static final TokenSet BLOCK_COMMENT_TOKENS = TokenSet.create(BLOCK_COMMENT, BLOCK_COMMENT_START, INCOMPLETE_BLOCK_COMMENT);
	private static final TokenSet DOC_COMMENT_TOKENS = TokenSet.create(DOC_COMMENT, INCOMPLETE_DOC_COMMENT);

	@SafeVarargs
	private static <T> Set<T> set(T... elements) {
		return new HashSet<>(Arrays.asList(elements));
	}

	private static final Set<IElementType> TYPE_TOKENS = set(
		BOOLEAN, BYTES, INT, STRING, FLOAT, DOUBLE, LONG,
		DATE, TIME_MS, TIMESTAMP_MS, LOCAL_TIMESTAMP_MS, DECIMAL, UUID,
		NULL, VOID, ARRAY, MAP, UNION);

	private static final Set<IElementType> KEYWORD_TOKENS = set(
		AvroIdlTypes.NAMESPACE, AvroIdlTypes.SCHEMA, AvroIdlTypes.PROTOCOL, AvroIdlTypes.IMPORT, AvroIdlTypes.IDL,
		AvroIdlTypes.RECORD, AvroIdlTypes.ERROR, AvroIdlTypes.ENUM, AvroIdlTypes.FIXED,
		AvroIdlTypes.THROWS, AvroIdlTypes.ONEWAY,
		AvroIdlTypes.TRUE, AvroIdlTypes.FALSE);

	@Override
    @NotNull
    public Lexer getHighlightingLexer() {
		return new AvroIdlLexer();
	}

	@Override
    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
		if (DOC_COMMENT_TOKENS.contains(tokenType)) {
			return DOC_COMMENT_KEYS;
		}
		if (BLOCK_COMMENT_TOKENS.contains(tokenType)) {
			return BLOCK_COMMENT_KEYS;
		}
		if (tokenType.equals(AvroIdlTypes.LINE_COMMENT)) {
			return LINE_COMMENT_KEYS;
		}
		if (tokenType.equals(TokenType.BAD_CHARACTER)) {
			return BAD_CHAR_KEYS;
		}
		if (tokenType.equals(AvroIdlTypes.STRING_LITERAL)) {
			return STRING_KEYS;
		}
		if (tokenType.equals(AvroIdlTypes.INT_LITERAL) || tokenType.equals(AvroIdlTypes.FLOAT_LITERAL)) {
			return NUMBER_KEYS;
		}
		if (tokenType.equals(AvroIdlTypes.LEFT_BRACE) || tokenType.equals(AvroIdlTypes.RIGHT_BRACE)) {
			return BRACES_KEYS;
		}
		if (tokenType.equals(AvroIdlTypes.LEFT_ANGLE) || tokenType.equals(AvroIdlTypes.RIGHT_ANGLE)) {
			return BRACES_KEYS;
		}
		if (tokenType.equals(AvroIdlTypes.LEFT_PAREN) || tokenType.equals(AvroIdlTypes.RIGHT_PAREN)) {
			return PARENTHESES_KEYS;
		}
		if (tokenType.equals(AvroIdlTypes.LEFT_BRACKET) || tokenType.equals(AvroIdlTypes.RIGHT_BRACKET)) {
			return BRACKETS_KEYS;
		}
		if (tokenType.equals(AvroIdlTypes.SEMICOLON)) {
			return SEMICOLON_KEYS;
		}
		if (tokenType.equals(AvroIdlTypes.COMMA)) {
			return COMMA_KEYS;
		}
		if (tokenType.equals(AvroIdlTypes.EQUALS)) {
			return EQUALS_KEYS;
		}
		if (TYPE_TOKENS.contains(tokenType)) {
			return TYPE_KEYS;
		}
		if (KEYWORD_TOKENS.contains(tokenType)) {
			return KEYWORD_KEYS;
		}
		return EMPTY_ARRAY;
	}
}
