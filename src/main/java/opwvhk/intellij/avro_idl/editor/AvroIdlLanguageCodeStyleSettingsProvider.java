package opwvhk.intellij.avro_idl.editor;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;
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
			consumer.renameStandardOption("SPACE_AROUND_ASSIGNMENT_OPERATORS", "Assignment operator (=)");
			consumer.moveStandardOption("SPACE_AFTER_COMMA_IN_TYPE_ARGUMENTS", "Within messages");
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
			consumer.renameStandardOption("BLANK_LINES_AROUND_CLASS", "Around named type");
			consumer.renameStandardOption("BLANK_LINES_AFTER_PACKAGE", "After namespace declaration");
		} else if (settingsType == SettingsType.LANGUAGE_SPECIFIC) {
			consumer.showCustomOption(AvroIdlCodeStyleSettings.class, "ADD_ALIAS_ON_SCHEMA_RENAME",
					"Add alias on schema rename", "Aliases");
			consumer.showCustomOption(AvroIdlCodeStyleSettings.class, "ADD_ALIAS_ON_FIELD_RENAME",
					"Add alias on field rename", "Aliases");
		}
	}

	@Override
	public IndentOptionsEditor getIndentOptionsEditor() {
		return new SmartIndentOptionsEditor(this);
	}

	@Override
	public String getCodeSample(@NotNull SettingsType settingsType) {
		return "namespace org.example;\n" +
				"\n" +
				"\n" +
				"\n" +
				"schema array<Employee>;\n" +
				"\n" +
				"\n" +
				"\n" +
				"import idl \"simple.avdl\";\n" +
				"\n" +
				"\n" +
				"\n" +
				"import idl \"reserved_words.avdl\";\n" +
				"\n" +
				"\n" +
				"\n" +
				" enum ContractType {\n" +
				"    TEMPORARY," +
				"\n" +
				"\n" +
				"\n" +
				"    FIXED\n" +
				"\n" +
				"\n" +
				"\n" +
				"} = TEMPORARY;\n" +
				"\n" +
				"\n" +
				"\n" +
				"@my-annotation([\"any\", \"json\"])\n" +
				"record Employee {\n" +
				"\n" +
				"\n" +
				"\n" +
				"    string name;\n" +
				"\n" +
				"\n" +
				"\n" +
				"    Employee? manager = null;\n" +
				"\n" +
				"\n" +
				"\n" +
				"    ContractType contract = \"FIXED\";\n" +
				"\n" +
				"\n" +
				"\n" +
				"    decimal(9, 2) salary;\n" +
				"\n" +
				"\n" +
				"\n" +
				"    array<string> skills = [];\n" +
				"\n" +
				"\n" +
				"\n" +
				"}";
	}
}
