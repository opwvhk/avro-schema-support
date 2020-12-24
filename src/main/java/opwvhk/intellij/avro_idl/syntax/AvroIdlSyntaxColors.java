package opwvhk.intellij.avro_idl.syntax;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.options.colors.AttributesDescriptor;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public interface AvroIdlSyntaxColors {
	TextAttributesKey DOC_COMMENT = createTextAttributesKey("AVROIDL_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT);
	TextAttributesKey BLOCK_COMMENT = createTextAttributesKey("AVROIDL_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
	TextAttributesKey LINE_COMMENT = createTextAttributesKey("AVROIDL_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	TextAttributesKey BAD_CHAR = createTextAttributesKey("AVROIDL_BAD_CHAR", HighlighterColors.BAD_CHARACTER);

	TextAttributesKey STRING = createTextAttributesKey("AVROIDL_STRING", DefaultLanguageHighlighterColors.STRING);
	TextAttributesKey NUMBER = createTextAttributesKey("AVROIDL_NUMBER", DefaultLanguageHighlighterColors.NUMBER);

	TextAttributesKey BRACES = createTextAttributesKey("AVROIDL_BRACES", DefaultLanguageHighlighterColors.BRACES);
	TextAttributesKey PARENTHESES = createTextAttributesKey("AVROIDL_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
	TextAttributesKey BRACKETS = createTextAttributesKey("AVROIDL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);

	TextAttributesKey SEMICOLON = createTextAttributesKey("AVROIDL_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
	TextAttributesKey COMMA = createTextAttributesKey("AVROIDL_COMMA", DefaultLanguageHighlighterColors.COMMA);
	TextAttributesKey EQUALS = createTextAttributesKey("AVROIDL_EQUALS", DefaultLanguageHighlighterColors.OPERATION_SIGN);

	TextAttributesKey KEYWORD = createTextAttributesKey("AVROIDL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	TextAttributesKey TYPE = createTextAttributesKey("AVROIDL_TYPE", DefaultLanguageHighlighterColors.KEYWORD);
	TextAttributesKey ANNOTATION = createTextAttributesKey("AVROIDL_ANNOTATION", DefaultLanguageHighlighterColors.METADATA);

	TextAttributesKey[] DOC_COMMENT_KEYS = new TextAttributesKey[]{DOC_COMMENT};
	TextAttributesKey[] BLOCK_COMMENT_KEYS = new TextAttributesKey[]{BLOCK_COMMENT};
	TextAttributesKey[] LINE_COMMENT_KEYS = new TextAttributesKey[]{LINE_COMMENT};
	TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHAR};
	TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
	TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
	TextAttributesKey[] BRACES_KEYS = new TextAttributesKey[]{BRACES};
	TextAttributesKey[] PARENTHESES_KEYS = new TextAttributesKey[]{PARENTHESES};
	TextAttributesKey[] BRACKETS_KEYS = new TextAttributesKey[]{BRACKETS};
	TextAttributesKey[] SEMICOLON_KEYS = new TextAttributesKey[]{SEMICOLON};
	TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[]{COMMA};
	TextAttributesKey[] EQUALS_KEYS = new TextAttributesKey[]{EQUALS};
	TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
	TextAttributesKey[] TYPE_KEYS = new TextAttributesKey[]{TYPE};

	AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
		new AttributesDescriptor("Documentation Comment", DOC_COMMENT),
		new AttributesDescriptor("Block Comment", BLOCK_COMMENT),
		new AttributesDescriptor("Line Comment", LINE_COMMENT),
		new AttributesDescriptor("Bad Character", BAD_CHAR),
		new AttributesDescriptor("String", STRING),
		new AttributesDescriptor("Number", NUMBER),
		new AttributesDescriptor("Braces", BRACES),
		new AttributesDescriptor("Parentheses", PARENTHESES),
		new AttributesDescriptor("Brackets", BRACKETS),
		new AttributesDescriptor("Semicolon", SEMICOLON),
		new AttributesDescriptor("Comma", COMMA),
		new AttributesDescriptor("Equals", EQUALS),
		new AttributesDescriptor("Keywords", KEYWORD),
		new AttributesDescriptor("Types", TYPE),
		new AttributesDescriptor("Annotations", ANNOTATION)
	};
}
