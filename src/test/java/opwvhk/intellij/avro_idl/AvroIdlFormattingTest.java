package opwvhk.intellij.avro_idl;

import com.intellij.application.options.CodeStyle;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.util.containers.ContainerUtil;

import java.util.function.BiConsumer;

public class AvroIdlFormattingTest extends LightJavaCodeInsightFixtureTestCase {
	@Override
	protected String getTestDataPath() {
		return "src/test/testData/formatting";
	}

	public void testCorrectFormattingNotBroken() {
		testDefaultFormatting("FullyReformatted.avdl");
	}

	public void testInputWithManyNewlines() {
		testDefaultFormatting("ManyNewlines.avdl");
	}

	public void testInputWithManySpaces() {
		testDefaultFormatting("ManySpaces.avdl");
	}

	public void testSpacelessInput() {
		testDefaultFormatting("Spaceless.avdl");
	}

	public void testEmptyLineCounts() {
		testFormatting("EmptyLinesInput.avdl", "EmptyLinesResultLineCounts_5_2_1_4_6.avdl", (codeStyle, indentStyle) -> {
			codeStyle.BLANK_LINES_BEFORE_IMPORTS = 5;
			codeStyle.BLANK_LINES_AFTER_IMPORTS = 2;
			codeStyle.BLANK_LINES_AROUND_CLASS = 1;
			codeStyle.BLANK_LINES_AROUND_FIELD = 4;
			codeStyle.BLANK_LINES_AROUND_METHOD = 6;
		});
	}

	public void testEmptyLinesToIndents() {
		testFormatting("EmptyLinesInput.avdl", "EmptyLinesResultIndents_2_1.avdl", (codeStyle, indentStyle) -> {
			codeStyle.KEEP_LINE_BREAKS = false; // Also allows testing continuation indent
			codeStyle.KEEP_BLANK_LINES_IN_DECLARATIONS = 2;
			codeStyle.KEEP_BLANK_LINES_IN_CODE = 1;
			// Don't add blank lines
			codeStyle.BLANK_LINES_AFTER_IMPORTS = 0;
			codeStyle.BLANK_LINES_AROUND_CLASS = 0;
			codeStyle.BLANK_LINES_AROUND_FIELD = 0;
			codeStyle.BLANK_LINES_AROUND_METHOD = 0;

			indentStyle.USE_TAB_CHARACTER = false;
			indentStyle.TAB_SIZE = 2;
			indentStyle.INDENT_SIZE = 2;
			indentStyle.CONTINUATION_INDENT_SIZE = 5;
		});
	}

	public void testSpaceIndentsAndBareEquals() {
		testFormatting("FullyReformatted.avdl", "SpaceIndentsAndBareEquals.avdl", (codeStyle, indentStyle) -> {
			codeStyle.SPACE_AROUND_ASSIGNMENT_OPERATORS = false;

			codeStyle.KEEP_LINE_BREAKS = false; // To test continuation indent

			indentStyle.USE_TAB_CHARACTER = false;
			indentStyle.TAB_SIZE = 2;
			indentStyle.INDENT_SIZE = 2;
			indentStyle.CONTINUATION_INDENT_SIZE = 5;
		});
	}


	private void testDefaultFormatting(String inputFile) {
		testFormatting(inputFile, "FullyReformatted.avdl", (ignored1, ignored2) -> {});
	}

	private void testFormatting(String inputFile, String outputFile,
								BiConsumer<CommonCodeStyleSettings, CommonCodeStyleSettings.IndentOptions> settingsAdjuster) {
		myFixture.configureByFile(inputFile);
		final CommonCodeStyleSettings avroIdlCodeStyleSettings = CodeStyle.getLanguageSettings(myFixture.getFile());
		final CommonCodeStyleSettings.IndentOptions avroIdlIndentOptions = avroIdlCodeStyleSettings.initIndentOptions();

		avroIdlIndentOptions.USE_TAB_CHARACTER = true;
		avroIdlIndentOptions.TAB_SIZE = 4;
		avroIdlIndentOptions.INDENT_SIZE = 4;
		avroIdlIndentOptions.CONTINUATION_INDENT_SIZE = 4;
		avroIdlIndentOptions.KEEP_INDENTS_ON_EMPTY_LINES = false;
		avroIdlCodeStyleSettings.RIGHT_MARGIN = 120;
		avroIdlCodeStyleSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS = true;
		avroIdlCodeStyleSettings.KEEP_LINE_BREAKS = false;
		avroIdlCodeStyleSettings.KEEP_BLANK_LINES_IN_CODE = 0;
		avroIdlCodeStyleSettings.KEEP_BLANK_LINES_IN_DECLARATIONS = 0;
		avroIdlCodeStyleSettings.BLANK_LINES_BEFORE_IMPORTS = 1;
		avroIdlCodeStyleSettings.BLANK_LINES_AFTER_IMPORTS = 1;
		avroIdlCodeStyleSettings.BLANK_LINES_AROUND_CLASS = 1; // Used for named types
		avroIdlCodeStyleSettings.BLANK_LINES_AROUND_FIELD = 0;
		avroIdlCodeStyleSettings.BLANK_LINES_AROUND_METHOD = 1;
		settingsAdjuster.accept(avroIdlCodeStyleSettings, avroIdlIndentOptions);
		WriteCommandAction.writeCommandAction(getProject()).run(() -> CodeStyleManager.getInstance(getProject()).reformatText(
			myFixture.getFile(), ContainerUtil.newArrayList(myFixture.getFile().getTextRange())));
		myFixture.checkResultByFile(outputFile);
	}
}
