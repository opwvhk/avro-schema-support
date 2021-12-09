package opwvhk.intellij.avro_idl;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import opwvhk.intellij.avro_idl.inspections.AvroIdlDuplicateAnnotationsInspectionTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AvroIdlCodeInsightTest extends LightJavaCodeInsightFixtureTestCase {

	@Override
	protected String getTestDataPath() {
		return "src/test/testData/codeInsight";
	}

	public void testAnnotator() {
		myFixture.configureByFiles("AllErrors.avdl");
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(myFixture.doHighlighting());
		// Note: because we're cutting out the text offsets, all error texts should be unique enough to be identified.
		// Luckily, the method returns highlights in the order they are in the file.
		assertOrderedEquals(highlight,
			Highlight.warning("/** Dangling documentation 1 */", "Dangling documentation comment"),
			Highlight.warning("\"12 monkeys\"", "The namespace is not composed of valid identifiers"),
			Highlight.warning("/** Dangling documentation 2 */", "Dangling documentation comment"),
			Highlight.error("Many.Mistakes", "Not a valid identifier: Many.Mistakes"),
			Highlight.warning("/** Dangling documentation 1 */", "Dangling documentation comment"),
			Highlight.warning("/** Dangling documentation 2 */", "Dangling documentation comment"),
			Highlight.error("Status", "Schema '12 monkeys.Status' is already defined"),
			Highlight.warning("/** Dangling documentation 1 */", "Dangling documentation comment"),
			Highlight.warning("/** Dangling documentation 2 */", "Dangling documentation comment"),
			Highlight.error("Status", "Schema '12 monkeys.Status' is already defined"),
			Highlight.warning("/** Dangling documentation 1 */", "Dangling documentation comment"),
			Highlight.error("12", "@namespace annotations must contain a string"),
			Highlight.warning("/** Dangling documentation 2 */", "Dangling documentation comment"),
			Highlight.warning("/** Dangling documentation 3 */", "Dangling documentation comment"),
			Highlight.error("foo.bar", "Not a valid identifier: foo.bar"),
			Highlight.warning("/** Dangling documentation 4 */", "Dangling documentation comment"),
			Highlight.warning("/** Dangling documentation 5 */", "Dangling documentation comment"),
			Highlight.warning("/** Dangling documentation 6 */", "Dangling documentation comment"),
			Highlight.warning("/** Dangling documentation 1 */", "Dangling documentation comment"),
			Highlight.error("\"even-more-wrong\"", "Not a valid identifier (with namespace): even-more-wrong"),
			Highlight.warning("/** Dangling documentation 2 */", "Dangling documentation comment"),
			Highlight.error("also-wrong", "Not a valid identifier: also-wrong"),
			Highlight.error("C", "Enum default must be one of the enum constants"),
			Highlight.error("my-data", "Not a valid identifier: my-data"),
			Highlight.error("@namespace(\"unused\")",
				"Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fail to compile."),
			Highlight.weakWarning("@logicalType(\"character\")", "A @logicalType annotation has no effect here"),
			Highlight.error("one-letter", "Not a valid identifier: one-letter"),
			Highlight.warning("/** Dangling documentation 1 */", "Dangling documentation comment"),
			Highlight.weakWarning("@namespace(\"on.type\")", "A @namespace annotation has no effect here"),
			Highlight.weakWarning("@aliases([\"for_type\"])", "An @aliases annotation has no effect here"),
			Highlight.warning("/** Dangling documentation 2 */", "Dangling documentation comment"),
			Highlight.weakWarning("@order(\"ignore\")", "An @order annotation has no effect here"),
			Highlight.error("23", "@logicalType annotation must contain a string naming the logical type"),
			Highlight.warning("/** Dangling documentation 3 */", "Dangling documentation comment"),
			Highlight.error("34", "@order annotation must contain one of: \"ascending\", \"descending\", \"ignore\""),
			Highlight.error("@logicalType(\"date\")", "The logical type 'date' requires the underlying type int"),
			Highlight.error("\"wrong\"", "@order annotation must contain one of: \"ascending\", \"descending\", \"ignore\""),
			Highlight.error("@logicalType(\"time-millis\")", "The logical type 'time-millis' requires the underlying type int"),
			Highlight.warning("/** Dangling documentation 1 */", "Dangling documentation comment"),
			Highlight.error("45", "@aliases annotations must contain an array of identifiers (strings)"),
			Highlight.warning("/** Dangling documentation 2 */", "Dangling documentation comment"),
			Highlight.error("@logicalType(\"timestamp-millis\")", "The logical type 'timestamp-millis' requires the underlying type long"),
			Highlight.error("56", "@aliases elements must be strings"),
			Highlight.error("\"invites-failure\"", "Not a valid identifier: invites-failure"),
			Highlight.error("@logicalType(\"local-timestamp-millis\")", "The logical type 'local-timestamp-millis' requires the underlying type long"),
			Highlight.error("@logicalType(\"decimal\")",
				"@logicalType(\"decimal\") requires a sibling @precision annotation with a number between 1 and 2^31-1"),
			Highlight.error("true", "@precision must contain a number between 1 and 2^31-1"),
			Highlight.error("-1", "@precision must contain a number between 1 and 2^31-1"),
			Highlight.error("4294967296", "@precision must contain a number between 1 and 2^31-1"),
			Highlight.error("false", "@scale must contain a non-negative number of at most the value of @precision"),
			Highlight.error("-2", "@scale must contain a non-negative number of at most the value of @precision"),
			Highlight.error("@logicalType(\"decimal\")", "The logical type 'decimal' requires the underlying type bytes or fixed"),
			Highlight.error("8", "@scale must contain a non-negative number of at most the value of @precision"),
			Highlight.error("@logicalType(\"decimal\")", "The logical type 'decimal' requires the underlying type bytes or fixed"),
			Highlight.error("@logicalType(\"decimal\")",
				"Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fail to compile."),
			Highlight.error("@precision(40)",
				"Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fail to compile."),
			Highlight.error("40", "hashes.MD5, a fixed(16), cannot store 40 digits (max 38)"),
			Highlight.error("@scale(0)", "Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fail to compile."),
			Highlight.warning("/** Dangling documentation 1 */", "Dangling documentation comment"),
			Highlight.error("67", "@aliases annotations must contain an array of identifiers (strings)"),
			Highlight.warning("/** Dangling documentation 2 */", "Dangling documentation comment"),
			Highlight.error("78", "@aliases elements must be strings"),
			Highlight.error("\"her-failure\"", "Not a valid identifier (with namespace): her-failure"),
			Highlight.warning("/** Dangling documentation 3 */", "Dangling documentation comment"),
			Highlight.error("his-failure", "Not a valid identifier: his-failure"),
			Highlight.warning("/** Dangling documentation 1 */", "Dangling documentation comment"),
			Highlight.error("Whatever", "Unknown schema: Whatever"),
			Highlight.error("do-stuff", "Not a valid identifier: do-stuff"),
			Highlight.warning("/** Dangling documentation 1 */", "Dangling documentation comment"),
			Highlight.error("Something", "Unknown schema: Something"),
			Highlight.warning("/** Dangling documentation 2 */", "Dangling documentation comment"),
			Highlight.error("SomeError", "Unknown schema: SomeError"),
			Highlight.error("hashes.MD5", "Not an error: hashes.MD5"),
			Highlight.error("oneway", "Oneway messages must have a void or null return type")
		);
	}

	public void testAnnotatorForImports() {
		myFixture.configureByFiles("ImportsAndReferences.avdl", "Employee.avdl", "ContractType.avsc", "HelloWorld.avpr");
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(myFixture.doHighlighting());
		// Note: because we're cutting out the text offsets, all error texts should be unique enough to be identified.
		// Luckily, the method returns highlights in the order they are in the file.
		assertContainsOrdered(highlight,
			Highlight.error("NotAnError", "Not an error: NotAnError")
		);
	}

	@SuppressWarnings("unchecked")
	public void testInspectionForDuplicateAnnotations() {
		myFixture.enableInspections(AvroIdlDuplicateAnnotationsInspectionTool.class);
		myFixture.configureByFiles("DuplicateAnnotations.avdl");
		final List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(highlightInfoList);
		assertContainsOrdered(highlight,
			Highlight.warning("@foo(\"bar1\")", "Duplicate annotation (only the last will take effect)"),
			Highlight.warning("@foo(\"bar2\")", "Duplicate annotation (only the last will take effect)"),
			Highlight.warning("@foo(\"bar3\")", "Duplicate annotation (only the last will take effect)")
		);
	}

	@SuppressWarnings("unchecked")
	public void testQuickFixForDuplicateAnnotations() {
		myFixture.enableInspections(AvroIdlDuplicateAnnotationsInspectionTool.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("DuplicateAnnotations.avdl");
		assertEquals(3, quickFixes.size());
		for (IntentionAction quickFix : quickFixes) {
			assertEquals("Delete all @foo annotations except the last.", quickFix.getText());
		}

		int index = new Random().nextInt(quickFixes.size());
		myFixture.launchAction(quickFixes.get(index));
		myFixture.checkResultByFile("DuplicateAnnotationsFixed.avdl");
	}

	@SuppressWarnings("unused")
	public void _testDocumentation() {
		myFixture.configureByFiles("DocumentationTestData.java", "DocumentationTestData.simple");
		final PsiElement originalElement = myFixture.getElementAtCaret();
		PsiElement element = DocumentationManager
			.getInstance(getProject())
			.findTargetElement(myFixture.getEditor(), originalElement.getContainingFile(), originalElement);

		if (element == null) {
			element = originalElement;
		}

		final DocumentationProvider documentationProvider = DocumentationManager.getProviderFromElement(element);
		final String generateDoc = documentationProvider.generateDoc(element, originalElement);
		assertNotNull(generateDoc);
		assertSameLinesWithFile(getTestDataPath() + "/" + "DocumentationTest.html.expected", generateDoc);
	}

	@SuppressWarnings("SameParameterValue")
	private static class Highlight {
		private final HighlightSeverity severity;
		private final String text;
		private final String description;

		static Highlight error(String text, String description) {
			return new Highlight(HighlightSeverity.ERROR, text, description);
		}

		static Highlight warning(String text, String description) {
			return new Highlight(HighlightSeverity.WARNING, text, description);
		}

		static Highlight weakWarning(String text, String description) {
			return new Highlight(HighlightSeverity.WEAK_WARNING, text, description);
		}

		static List<Highlight> fromHighlightInfoList(List<HighlightInfo> highlightInfoList) {
			List<Highlight> result = new ArrayList<>(highlightInfoList.size());
			for (HighlightInfo highlightInfo : highlightInfoList) {
				result.add(new Highlight(highlightInfo.getSeverity(), highlightInfo.getText(), highlightInfo.getDescription()));
			}
			return result;
		}

		private Highlight(HighlightSeverity severity, String text, String description) {
			this.severity = Objects.requireNonNull(severity);
			this.text = Objects.requireNonNull(text);
			this.description = Objects.requireNonNull(description);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Highlight highlight = (Highlight)o;
			return severity.equals(highlight.severity) &&
				text.equals(highlight.text) &&
				description.equals(highlight.description);
		}

		@Override
		public int hashCode() {
			return Objects.hash(severity, text, description);
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> %s", severity.getName().toLowerCase(), text, description);
		}
	}
}

