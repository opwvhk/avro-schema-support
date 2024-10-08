package opwvhk.intellij.avro_idl.editor;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import opwvhk.intellij.avro_idl.TextBundle;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public interface AvroIdlSyntaxColors {
	TextAttributesKey DOC_COMMENT = createTextAttributesKey("AVRO_IDL_DOC_COMMENT",
			DefaultLanguageHighlighterColors.DOC_COMMENT);
	TextAttributesKey BLOCK_COMMENT = createTextAttributesKey("AVRO_IDL_BLOCK_COMMENT",
			DefaultLanguageHighlighterColors.BLOCK_COMMENT);
	TextAttributesKey LINE_COMMENT = createTextAttributesKey("AVRO_IDL_LINE_COMMENT",
			DefaultLanguageHighlighterColors.LINE_COMMENT);
	TextAttributesKey BAD_CHAR = createTextAttributesKey("AVRO_IDL_BAD_CHAR", HighlighterColors.BAD_CHARACTER);

	TextAttributesKey STRING = createTextAttributesKey("AVRO_IDL_STRING", DefaultLanguageHighlighterColors.STRING);
	TextAttributesKey NUMBER = createTextAttributesKey("AVRO_IDL_NUMBER", DefaultLanguageHighlighterColors.NUMBER);

	TextAttributesKey BRACES = createTextAttributesKey("AVRO_IDL_BRACES", DefaultLanguageHighlighterColors.BRACES);
	TextAttributesKey PARENTHESES = createTextAttributesKey("AVRO_IDL_PARENTHESES",
			DefaultLanguageHighlighterColors.PARENTHESES);
	TextAttributesKey BRACKETS = createTextAttributesKey("AVRO_IDL_BRACKETS",
			DefaultLanguageHighlighterColors.BRACKETS);

	TextAttributesKey SEMICOLON = createTextAttributesKey("AVRO_IDL_SEMICOLON",
			DefaultLanguageHighlighterColors.SEMICOLON);
	TextAttributesKey COMMA = createTextAttributesKey("AVRO_IDL_COMMA", DefaultLanguageHighlighterColors.COMMA);
	TextAttributesKey EQUALS = createTextAttributesKey("AVRO_IDL_EQUALS",
			DefaultLanguageHighlighterColors.OPERATION_SIGN);

	TextAttributesKey KEYWORD = createTextAttributesKey("AVRO_IDL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	TextAttributesKey TYPE = createTextAttributesKey("AVRO_IDL_TYPE", DefaultLanguageHighlighterColors.KEYWORD);
	TextAttributesKey ANNOTATION = createTextAttributesKey("AVRO_IDL_ANNOTATION",
			DefaultLanguageHighlighterColors.METADATA);

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
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.documentation.comment"), DOC_COMMENT),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.block.comment"), BLOCK_COMMENT),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.line.comment"), LINE_COMMENT),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.bad.character"), BAD_CHAR),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.string"), STRING),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.number"), NUMBER),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.braces"), BRACES),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.parentheses"), PARENTHESES),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.brackets"), BRACKETS),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.semicolon"), SEMICOLON),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.comma"), COMMA),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.equals"), EQUALS),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.keywords"), KEYWORD),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.types"), TYPE),
			new AttributesDescriptor(TextBundle.message("options.AvroIdl.color.descriptor.annotations"), ANNOTATION)	};
}
