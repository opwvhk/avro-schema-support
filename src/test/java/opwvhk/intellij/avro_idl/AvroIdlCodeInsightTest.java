package opwvhk.intellij.avro_idl;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.spellchecker.SpellCheckerSeveritiesProvider;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import opwvhk.intellij.avro_idl.inspections.*;

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
			Highlight.warning("\"12 monkeys\"", "The namespace is not composed of valid identifiers"),
			Highlight.error("Many.Mistakes", "Not a valid identifier: Many.Mistakes"),
			Highlight.error("Status", "Schema '12 monkeys.Status' is already defined"),
			Highlight.error("Status", "Schema '12 monkeys.Status' is already defined"),
			Highlight.error("12", "@namespace annotations must contain a string"),
			Highlight.error("foo.bar", "Not a valid identifier: foo.bar"),
			Highlight.error("\"even-more-wrong\"", "Not a valid identifier (with namespace): even-more-wrong"),
			Highlight.error("also-wrong", "Not a valid identifier: also-wrong"),
			Highlight.error("C", "Enum default must be one of the enum constants"),
			Highlight.error("my-data", "Not a valid identifier: my-data"),
			Highlight.error("@namespace(\"unused\")", "Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fail to compile."),
			Highlight.error("one-letter", "Not a valid identifier: one-letter"),
			Highlight.error("23", "@logicalType annotation must contain a string naming the logical type"),
			Highlight.error("34", "@order annotation must contain one of: \"ascending\", \"descending\", \"ignore\""),
			Highlight.error("@logicalType(\"date\")", "The logical type 'date' requires the underlying type int"),
			Highlight.error("\"wrong\"", "@order annotation must contain one of: \"ascending\", \"descending\", \"ignore\""),
			Highlight.error("@logicalType(\"time-millis\")", "The logical type 'time-millis' requires the underlying type int"),
			Highlight.error("45", "@aliases annotations must contain an array of identifiers (strings)"),
			Highlight.error("@logicalType(\"timestamp-millis\")", "The logical type 'timestamp-millis' requires the underlying type long"),
			Highlight.error("56", "@aliases elements must be strings"),
			Highlight.error("\"invites-failure\"", "Not a valid identifier: invites-failure"),
			Highlight.error("@logicalType(\"local-timestamp-millis\")", "The logical type 'local-timestamp-millis' requires the underlying type long"),
			Highlight.error("@logicalType(\"decimal\")", "@logicalType(\"decimal\") requires a sibling @precision annotation with a number between 1 and 2^31-1"),
			Highlight.error("true", "@precision must contain a number between 1 and 2^31-1"),
			Highlight.error("-1", "@precision must contain a number between 1 and 2^31-1"),
			Highlight.error("4294967296", "@precision must contain a number between 1 and 2^31-1"),
			Highlight.error("false", "@scale must contain a non-negative number of at most the value of @precision"),
			Highlight.error("-2", "@scale must contain a non-negative number of at most the value of @precision"),
			Highlight.error("@logicalType(\"decimal\")", "The logical type 'decimal' requires the underlying type bytes or fixed"),
			Highlight.error("8", "@scale must contain a non-negative number of at most the value of @precision"),
			Highlight.error("@logicalType(\"decimal\")", "The logical type 'decimal' requires the underlying type bytes or fixed"),
			Highlight.error("@logicalType(\"decimal\")", "The logical type 'decimal' requires the underlying type bytes or fixed"),
			Highlight.error("@logicalType(\"decimal\")", "Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fail to compile."),
			Highlight.error("@precision(40)", "Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fail to compile."),
			Highlight.error("40", "hashes.MD5, a fixed(16), cannot store 40 digits (max 38)"),
			Highlight.error("@scale(0)", "Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fail to compile."),
			Highlight.error("67", "@aliases annotations must contain an array of identifiers (strings)"),
			Highlight.error("78", "@aliases elements must be strings"),
			Highlight.error("\"her-failure\"", "Not a valid identifier (with namespace): her-failure"),
			Highlight.error("his-failure", "Not a valid identifier: his-failure"),
			Highlight.error("Whatever", "Unknown schema: Whatever"),
			Highlight.error("do-stuff", "Not a valid identifier: do-stuff"),
			Highlight.error("Something", "Unknown schema: Something"),
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
	public void testDuplicateAnnotationsInspection() {
		myFixture.enableInspections(AvroIdlDuplicateAnnotationsInspection.class);
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
	public void testDuplicateAnnotationsInspectionQuickFix() {
		myFixture.enableInspections(AvroIdlDuplicateAnnotationsInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("DuplicateAnnotations.avdl");
		assertEquals(3, quickFixes.size());
		for (IntentionAction quickFix : quickFixes) {
			assertEquals("Delete all @foo annotations except the last", quickFix.getText());
		}

		int index = new Random().nextInt(quickFixes.size());
		myFixture.launchAction(quickFixes.get(index));
		myFixture.checkResultByFile("DuplicateAnnotationsFixed.avdl");
	}

	@SuppressWarnings("unchecked")
	public void testUseNullableShorthandInspection() {
		myFixture.enableInspections(AvroIdlUseNullableShorthandInspection.class);
		myFixture.configureByFiles("AllowShorthandNullable.avdl");
		final List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(highlightInfoList);
		assertContainsOrdered(highlight,
			Highlight.weakWarning("union{null, string}", "Union can be simplified")
		);
	}

	@SuppressWarnings("unchecked")
	public void testUseNullableShorthandInspectionQuickFix() {
		myFixture.enableInspections(AvroIdlUseNullableShorthandInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("AllowShorthandNullable.avdl");
		assertEquals(1, quickFixes.size());
		IntentionAction quickFix = quickFixes.get(0);
		assertEquals("Replace union with shorthand notation", quickFix.getText());
		myFixture.launchAction(quickFix);
		myFixture.checkResultByFile("AllowShorthandNullableFixed.avdl");
	}

	@SuppressWarnings("unchecked")
	public void testMisplacedAnnotationsInspection() {
		myFixture.enableInspections(AvroIdlMisplacedAnnotationsInspection.class);
		myFixture.configureByFiles("MisplacedAnnotations.avdl");
		final List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(highlightInfoList);
		assertContainsOrdered(highlight,
			Highlight.warning("@logicalType(\"character\")", "The @logicalType annotation has no effect here"),
			Highlight.warning("@namespace(\"on.type\")", "The @namespace annotation has no effect here"),
			Highlight.warning("@aliases([\"for_type\"])", "The @aliases annotation has no effect here"),
			Highlight.warning("@order(\"ignore\")", "The @order annotation has no effect here")
		);
	}

	@SuppressWarnings("unchecked")
	public void testMisplacedDocumentationInspection() {
		myFixture.enableInspections(AvroIdlMisplacedDocumentationInspection.class);
		myFixture.configureByFiles("MisplacedDocumentation.avdl");
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(myFixture.doHighlighting());

		// Note: because we're cutting out the text offsets, all error texts should be unique enough to be identified.
		// Luckily, the method returns highlights in the order they are in the file.

		final String danglingDocumentationDescription = "Dangling documentation comment: the next documentation comments causes this one to be ignored";
		final String misplacedDocumentationDescription = "Misplaced documentation comment: documentation comments should be placed before declarations";
		assertOrderedEquals(highlight,
			Highlight.warning("/** Dangling documentation 1 */", danglingDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 1 */", misplacedDocumentationDescription),
			Highlight.warning("/** Dangling documentation 2 */", danglingDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 2 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 3 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 4 */", misplacedDocumentationDescription),
			Highlight.warning("/** Dangling documentation 3 */", danglingDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 5 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 6 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 7 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 8 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 9 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 10 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 11 */", misplacedDocumentationDescription),
			Highlight.warning("/** Dangling documentation 4 */", danglingDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 12 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 13 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 14 */", misplacedDocumentationDescription),
			Highlight.warning("/** Dangling documentation 5 */", danglingDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 15 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 16 */", misplacedDocumentationDescription),
			Highlight.warning("/** Dangling documentation 6 */", danglingDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 17 */", misplacedDocumentationDescription),
			Highlight.warning("/** Misplaced documentation 18 */", misplacedDocumentationDescription),
			Highlight.warning("/** Dangling documentation 7 */", danglingDocumentationDescription),
			Highlight.warning("/** Dangling documentation 8 */", danglingDocumentationDescription)
		);
	}

	@SuppressWarnings("unchecked")
	public void testNamingInspection() {
		myFixture.enableInspections(AvroIdlNamingConventionInspection.class);
		myFixture.configureByFiles("Naming.avdl");
		final List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(highlightInfoList);
		assertContainsOrdered(highlight,
			Highlight.typo("\"Package\"", "Namespace name '\"Package\"' doesn't match regex '[a-z][a-z0-9_]*[a-z0-9](\\.[a-z][a-z0-9_]*[a-z0-9])*'"),
			Highlight.typo("mis_named", "Protocol name 'mis_named' doesn't match regex '[A-Z][a-zA-Z0-9]*'"),
			Highlight.typo("lowerCaseStart", "Fixed name 'lowerCaseStart' doesn't match regex '[A-Z][a-zA-Z0-9]*'"),
			Highlight.typo("status", "Enum name 'status' doesn't match regex '[A-Z][a-zA-Z0-9]*'"),
			Highlight.typo("on", "Enum constant name 'on' is too short (2 < 3)"),
			Highlight.typo("off", "Enum constant name 'off' doesn't match regex '[A-Z][A-Z0-9_]*[A-Z0-9]'"),
			Highlight.typo("Name", "Fixed name 'Name' doesn't match regex '[a-z][a-zA-Z0-9]*'"),
			Highlight.typo("failure", "Record name 'failure' doesn't match regex '[A-Z][a-zA-Z0-9]*'"),
			Highlight.typo("Cause", "Fixed name 'Cause' doesn't match regex '[a-z][a-zA-Z0-9]*'"),
			Highlight.typo("Ping", "Message name 'Ping' doesn't match regex '[a-z][a-zA-Z0-9]*'")
		);
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

		static Highlight typo(String text, String description) {
			return new Highlight(SpellCheckerSeveritiesProvider.TYPO, text, description);
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

