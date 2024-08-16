package opwvhk.intellij.avro_idl.editor;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import opwvhk.intellij.avro_idl.TextBundle;
import org.jetbrains.annotations.NotNull;

public class AvroIdlLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

	@Override
	@NotNull
	public Language getLanguage() {
		return AvroIdlLanguage.INSTANCE;
	}

	@Override
	protected void customizeDefaults(@NotNull CommonCodeStyleSettings commonSettings,
	                                 @NotNull CommonCodeStyleSettings.IndentOptions indentOptions) {

		// Opinionated defaults.

		indentOptions.USE_TAB_CHARACTER = true;
		indentOptions.TAB_SIZE = 4;
		indentOptions.INDENT_SIZE = indentOptions.TAB_SIZE;
		indentOptions.CONTINUATION_INDENT_SIZE = indentOptions.TAB_SIZE * 2;
		indentOptions.KEEP_INDENTS_ON_EMPTY_LINES = false;

		commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS = true;
		commonSettings.SPACE_AFTER_COMMA = true;
		commonSettings.SPACE_AFTER_COMMA_IN_TYPE_ARGUMENTS = true;
		commonSettings.KEEP_LINE_BREAKS = true; // Keep programmer in charge of line breaks; reformatting twice overrides this anyway.
		commonSettings.KEEP_BLANK_LINES_IN_CODE = 0;
		commonSettings.KEEP_BLANK_LINES_IN_DECLARATIONS = 1;
		commonSettings.BLANK_LINES_AFTER_PACKAGE = 1; // Namespace declaration
		commonSettings.BLANK_LINES_BEFORE_IMPORTS = 1;
		commonSettings.BLANK_LINES_AFTER_IMPORTS = 1;
		commonSettings.BLANK_LINES_AROUND_CLASS = 1; // Named types & main schema declaration
		commonSettings.BLANK_LINES_AROUND_FIELD = 0;
		commonSettings.BLANK_LINES_AROUND_METHOD = 1;

		// These settings must NOT be named in customizeSettings! They should not be changed. Changing them is possible, but the results are undefined.
		commonSettings.BLOCK_COMMENT_AT_FIRST_COLUMN = false;
		commonSettings.LINE_COMMENT_AT_FIRST_COLUMN = false;
		commonSettings.LINE_COMMENT_ADD_SPACE = false;
	}

	@Override
	public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
		if (settingsType == SettingsType.INDENT_SETTINGS) {
			consumer.showStandardOptions("USE_TAB_CHARACTER", "TAB_SIZE", "INDENT_SIZE", "CONTINUATION_INDENT_SIZE",
					"KEEP_INDENTS_ON_EMPTY_LINES");
		} else if (settingsType == SettingsType.SPACING_SETTINGS) {
			consumer.showStandardOptions("SPACE_AROUND_ASSIGNMENT_OPERATORS",
					"SPACE_AFTER_COMMA",
					"SPACE_AFTER_COMMA_IN_TYPE_ARGUMENTS");
			consumer.renameStandardOption("SPACE_AROUND_ASSIGNMENT_OPERATORS",
					TextBundle.message("code.style.space_around_assignment_operators.title"));
			consumer.moveStandardOption("SPACE_AFTER_COMMA_IN_TYPE_ARGUMENTS",
					TextBundle.message("code.style.space_after_comma_in_type_arguments.group"));
		} else if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
			consumer.showStandardOptions("RIGHT_MARGIN", "WRAP_ON_TYPING", "KEEP_LINE_BREAKS");
		} else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
			consumer.showStandardOptions(
					"KEEP_BLANK_LINES_IN_CODE"
					, "KEEP_BLANK_LINES_IN_DECLARATIONS"
					, "BLANK_LINES_AFTER_PACKAGE"
					, "BLANK_LINES_AFTER_IMPORTS"
					, "BLANK_LINES_AROUND_CLASS"
					, "BLANK_LINES_AROUND_FIELD"
					, "BLANK_LINES_AROUND_METHOD"
			);
			consumer.renameStandardOption("BLANK_LINES_AROUND_CLASS",
					TextBundle.message("code.style.blank_lines_around_class.title"));
			consumer.renameStandardOption("BLANK_LINES_AFTER_PACKAGE",
					TextBundle.message("code.style.blank_lines_after_package.title"));
		} else if (settingsType == SettingsType.LANGUAGE_SPECIFIC) {
			consumer.showCustomOption(AvroIdlCodeStyleSettings.class, "ADD_ALIAS_ON_SCHEMA_RENAME",
					TextBundle.message("code.style.add_alias_on_schema_rename.title"),
					TextBundle.message("code.style.add_alias_on_schema_rename.group"));
			consumer.showCustomOption(AvroIdlCodeStyleSettings.class, "ADD_ALIAS_ON_FIELD_RENAME",
					TextBundle.message("code.style.add_alias_on_field_rename.title"),
					TextBundle.message("code.style.add_alias_on_field_rename.group"));
		}
	}

	@Override
	public IndentOptionsEditor getIndentOptionsEditor() {
		return new SmartIndentOptionsEditor(this);
	}

	@Override
	public String getCodeSample(@NotNull SettingsType settingsType) {
		//noinspection InconsistentTextBlockIndent
		return """
		       namespace org.example;



		       schema array<Employee>;



		       import idl "simple.avdl";



		       import idl "reserved_words.avdl";



		        enum ContractType {
		           TEMPORARY,\



		           FIXED



		       } = TEMPORARY;



		       @my-annotation(["any", "json"])
		       record Employee {



		           string name;



		           Employee? manager = null;



		           ContractType contract = "FIXED";



		           decimal(9, 2) salary;



		           array<string> skills = [];



		       }""";
	}
}
