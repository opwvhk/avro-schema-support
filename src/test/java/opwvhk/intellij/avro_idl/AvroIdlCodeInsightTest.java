package opwvhk.intellij.avro_idl;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.spellchecker.SpellCheckerSeveritiesProvider;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import opwvhk.intellij.avro_idl.inspections.*;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class AvroIdlCodeInsightTest extends BasePlatformTestCase {
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
				Highlight.error("Many-Mistakes", "Not a valid identifier: Many-Mistakes"),
				Highlight.error("Status", "Schema \"12 monkeys.Status\" is already defined"),
				Highlight.error("Status", "Schema \"12 monkeys.Status\" is already defined"),
				Highlight.error("12", "@namespace annotations must contain a string"),
				Highlight.error("\"even-more-wrong\"", "Not a valid identifier (with namespace): even-more-wrong"),
				Highlight.error("also-wrong", "Not a valid identifier: also-wrong"),
				Highlight.error("C", "Enum default must be one of the enum constants"),
				Highlight.error("my-data", "Not a valid identifier: my-data"),
				Highlight.error("duplicate", "Field \"duplicate\" is already defined"),
				Highlight.error("duplicate", "Field \"duplicate\" is already defined"),
				// 11
				Highlight.error("@namespace(\"unused\")",
						"Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fails to compile."),
				Highlight.error("one-letter", "Not a valid identifier: one-letter"),
				Highlight.error("23", "@logicalType annotation must contain a string naming the logical type"),
				Highlight.error("34",
						"@order annotation must contain one of: \"ascending\", \"descending\", \"ignore\""),
				Highlight.error("@logicalType(\"date\")",
						"The logical type \"date\" requires the underlying type \"int\""),
				Highlight.error("\"wrong\"",
						"@order annotation must contain one of: \"ascending\", \"descending\", \"ignore\""),
				Highlight.error("@logicalType(\"time-millis\")",
						"The logical type \"time-millis\" requires the underlying type \"int\""),
				Highlight.error("45", "@aliases annotations must contain an array of identifiers (strings)"),
				Highlight.error("@logicalType(\"timestamp-millis\")",
						"The logical type \"timestamp-millis\" requires the underlying type \"long\""),
				Highlight.error("56", "@aliases elements must be strings"),
				// 21
				Highlight.error("\"invites-failure\"", "Not a valid identifier: invites-failure"),
				Highlight.error("@logicalType(\"local-timestamp-millis\")",
						"The logical type \"local-timestamp-millis\" requires the underlying type \"long\""),
				Highlight.error("@logicalType(\"decimal\")",
						"@logicalType(\"decimal\") requires a sibling @precision annotation with a number between 1 and 2^31-1"),
				Highlight.error("true", "@precision must contain a number between 1 and 2^31-1"),
				Highlight.error("-1", "@precision must contain a number between 1 and 2^31-1"),
				Highlight.error("4294967296", "@precision must contain a number between 1 and 2^31-1"),
				Highlight.error("false",
						"@scale must contain a non-negative number of at most the value of @precision"),
				Highlight.error("-2", "@scale must contain a non-negative number of at most the value of @precision"),
				Highlight.error("@logicalType(\"decimal\")",
						"The logical type \"decimal\" requires the underlying type \"bytes\" or \"fixed\""),
				Highlight.error("8", "@scale must contain a non-negative number of at most the value of @precision"),
				// 31
				Highlight.error("@logicalType(\"decimal\")",
						"The logical type \"decimal\" requires the underlying type \"bytes\" or \"fixed\""),
				// Swap these two when using <2024.3
				Highlight.error("@logicalType(\"decimal\")",
						"Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fails to compile."),
				Highlight.error("@logicalType(\"decimal\")",
						"The logical type \"decimal\" requires the underlying type \"bytes\" or \"fixed\""),
				Highlight.error("@precision(40)",
						"Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fails to compile."),
				Highlight.error("40", "hashes.MD5, a fixed(16), cannot store 40 digits (max 38)"),
				Highlight.error("@scale(0)",
						"Type references must not be annotated: Avro < 1.11.1 changes the referenced type, Avro >= 1.11.1 fails to compile."),
				Highlight.error("@logicalType(\"duration\")",
						"The logical type \"duration\" requires the underlying type \"fixed\", of 12 bytes"),
				Highlight.error("@logicalType(\"duration\")",
						"The logical type \"duration\" requires the underlying type \"fixed\", of 12 bytes"),
				Highlight.error("DUP", "Enum constant \"DUP\" is already defined"),
				Highlight.error("DUP", "Enum constant \"DUP\" is already defined"),
				Highlight.error("67", "@aliases annotations must contain an array of identifiers (strings)"),
				Highlight.error("78", "@aliases elements must be strings"),
				Highlight.error("\"her-failure\"", "Not a valid identifier (with namespace): her-failure"),
				Highlight.error("his-failure", "Not a valid identifier: his-failure"),
				Highlight.error("Whatever", "Unknown schema: Whatever"),
				Highlight.error("do-stuff", "Not a valid identifier: do-stuff"),
				Highlight.error("Something", "Unknown schema: Something"),
				Highlight.error("one", "Message parameter \"one\" is already defined"),
				Highlight.error("Something", "Unknown schema: Something"),
				Highlight.error("one", "Message parameter \"one\" is already defined"),
				Highlight.error("SomeError", "Unknown schema: SomeError"),
				Highlight.error("hashes.MD5", "Not an error: hashes.MD5"),
				Highlight.error("oneway", "Oneway messages must have a void or null return type")
		);
	}

	public void testAnnotatorForImports() {
		myFixture.configureByFiles("ImportsAndReferences.avdl", "Employee.avdl", "ContractType.avsc",
				"HelloWorld.avpr");
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(myFixture.doHighlighting());
		// Note: because we're cutting out the text offsets, all error texts should be unique enough to be identified.
		// Luckily, the method returns highlights in the order they are in the file.
		assertContainsOrdered(highlight,
				Highlight.error("NotAnError", "Not an error: NotAnError")
		);
	}

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

	public void testUseNullableShorthandInspection() {
		myFixture.enableInspections(AvroIdlUseNullableShorthandInspection.class);
		myFixture.configureByFiles("AllowShorthandNullable.avdl");
		final List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(highlightInfoList);
		assertContainsOrdered(highlight,
				Highlight.weakWarning("union{null, string}", "Union can be simplified"),
				Highlight.weakWarning("union{null, string}", "Union can be simplified"),
				Highlight.weakWarning("union{string, null}", "Union can be simplified")
		);
	}

	public void testUseNullableShorthandInspectionQuickFix() {
		myFixture.enableInspections(AvroIdlUseNullableShorthandInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("AllowShorthandNullable.avdl");
		assertEquals(3, quickFixes.size());
		for (IntentionAction quickFix : quickFixes) {
			assertEquals("Replace union with shorthand notation", quickFix.getText());
			myFixture.launchAction(quickFix);
		}
		myFixture.checkResultByFile("AllowShorthandNullableFixed.avdl");
	}

	public void testAvoidNullableShorthandInspection() {
		myFixture.enableInspections(AvroIdlAvoidNullableShorthandInspection.class);
		myFixture.configureByFiles("AllowShorthandNullableFixed.avdl");
		final List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(highlightInfoList);
		assertContainsOrdered(highlight,
				Highlight.error("string?", "Use union instead of optional type"),
				Highlight.error("string?", "Use union instead of optional type"),
				Highlight.error("string?", "Use union instead of optional type")
		);
	}

	public void testAvoidNullableShorthandInspectionQuickFix() {
		myFixture.enableInspections(AvroIdlAvoidNullableShorthandInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("AllowShorthandNullableFixed.avdl");
		assertEquals(3, quickFixes.size());
		for (IntentionAction quickFix : quickFixes) {
			assertEquals("Replace with union of type with null", quickFix.getText());
			myFixture.launchAction(quickFix);
		}
		myFixture.checkResultByFile("AllowShorthandNullable.avdl");
	}

	public void testMisplacedAnnotationsInspection() {
		myFixture.enableInspections(AvroIdlMisplacedAnnotationsInspection.class);
		myFixture.configureByFiles("MisplacedAnnotations.avdl");
		final List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(highlightInfoList);
		assertContainsOrdered(highlight,
				Highlight.weakWarning("@logicalType(\"character\")", "The @logicalType annotation has no effect here"),
				Highlight.weakWarning("@namespace(\"on.type\")", "The @namespace annotation has no effect here"),
				Highlight.weakWarning("@aliases([\"for_type\"])", "The @aliases annotation has no effect here"),
				Highlight.weakWarning("@order(\"ignore\")", "The @order annotation has no effect here")
		);
	}

	public void testMisplacedDocumentationInspection() {
		myFixture.enableInspections(AvroIdlMisplacedDocumentationInspection.class);
		myFixture.configureByFiles("MisplacedDocumentation.avdl");
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(myFixture.doHighlighting());

		// Note: because we're cutting out the text offsets, all error texts should be unique enough to be identified.
		// Luckily, the method returns highlights in the order they are in the file.

		final String description = "Misplaced documentation comment: documentation comments should be placed directly before declarations";
		assertOrderedEquals(highlight,
				Highlight.warning("/** Dangling documentation 1 */", description),
				Highlight.warning("/** Misplaced documentation 1 */", description),
				Highlight.warning("/** Dangling documentation 2 */", description),
				Highlight.warning("/** Misplaced documentation 2 */", description),
				Highlight.warning("/** Misplaced documentation 3 */", description),
				Highlight.warning("/** Misplaced documentation 4 */", description),
				Highlight.warning("/** Dangling documentation 3 */", description),
				Highlight.warning("/** Misplaced documentation 5 */", description),
				Highlight.warning("/** Misplaced documentation 6 */", description),
				Highlight.warning("/** Misplaced documentation 7 */", description),
				Highlight.warning("/** Misplaced documentation 8 */", description),
				Highlight.warning("/** Misplaced documentation 9 */", description),
				Highlight.warning("/** Misplaced documentation 10 */", description),
				Highlight.warning("/** Misplaced documentation 11 */", description),
				Highlight.warning("/** Dangling documentation 4 */", description),
				Highlight.warning("/** Misplaced documentation 12 */", description),
				Highlight.warning("/** Misplaced documentation 13 */", description),
				Highlight.warning("/** Misplaced documentation 14 */", description),
				Highlight.warning("/** Dangling documentation 5 */", description),
				Highlight.warning("/** Misplaced documentation 15 */", description),
				Highlight.warning("/** Misplaced documentation 16 */", description),
				Highlight.warning("/** Dangling documentation 6 */", description),
				Highlight.warning("/** Misplaced documentation 17 */", description),
				Highlight.warning("/** Misplaced documentation 18 */", description),
				Highlight.warning("/** Dangling documentation 7 */", description),
				Highlight.warning("/** Dangling documentation 8 */", description)
		);
	}

	public void testMisplacedDocumentationInspectionQuickFixRemove() {
		myFixture.enableInspections(AvroIdlMisplacedDocumentationInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("MisplacedDocumentation.avdl");
		assertEquals(52, quickFixes.size());
		for (int i = 0; i < quickFixes.size(); i += 2) {
			IntentionAction quickFix = quickFixes.get(i);
			assertEquals("Delete misplaced documentation comment", quickFix.getText());
			myFixture.launchAction(quickFix);
		}
		myFixture.checkResultByFile("MisplacedDocumentationRemoved.avdl");
	}

	public void testMisplacedDocumentationInspectionQuickFixChange() {
		myFixture.enableInspections(AvroIdlMisplacedDocumentationInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("MisplacedDocumentation.avdl");
		assertEquals(52, quickFixes.size());
		for (int i = 1; i < quickFixes.size(); i += 2) {
			IntentionAction quickFix = quickFixes.get(i);
			assertEquals("Replace with multiline comment", quickFix.getText());
			myFixture.launchAction(quickFix);
		}
		myFixture.checkResultByFile("MisplacedDocumentationFixed.avdl");
	}

	public void testNamingInspection() {
		myFixture.enableInspections(AvroIdlNamingConventionInspection.class);
		myFixture.configureByFiles("Naming.avdl");
		final List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(highlightInfoList);
		assertContainsOrdered(highlight,
				Highlight.typo("\"Package\"",
						"Namespace name '\"Package\"' doesn't match regex '[a-z][a-z0-9_]*[a-z0-9](\\.[a-z][a-z0-9_]*[a-z0-9])*'"),
				Highlight.typo("mis_named", "Protocol name 'mis_named' doesn't match regex '[A-Z][a-zA-Z0-9]*'"),
				Highlight.typo("lowerCaseStart", "Fixed name 'lowerCaseStart' doesn't match regex '[A-Z][a-zA-Z0-9]*'"),
				Highlight.typo("status", "Enum name 'status' doesn't match regex '[A-Z][a-zA-Z0-9]*'"),
				Highlight.typo("on", "Enum constant name 'on' is too short (2 < 3)"),
				Highlight.typo("off", "Enum constant name 'off' doesn't match regex '[A-Z][A-Z0-9_]*[A-Z0-9]'"),
				Highlight.typo("Name", "Field name 'Name' doesn't match regex '[a-z][a-zA-Z0-9]*'"),
				Highlight.typo("failure", "Error name 'failure' doesn't match regex '[A-Z][a-zA-Z0-9]*'"),
				Highlight.typo("Cause", "Field name 'Cause' doesn't match regex '[a-z][a-zA-Z0-9]*'"),
				Highlight.typo("Ping", "Message name 'Ping' doesn't match regex '[a-z][a-zA-Z0-9]*'")
		);
	}

	public void testSchemaSyntaxInspectionWithoutNamespaceOrSchema() {
		myFixture.enableInspections(AvroIdlUseSchemaSyntaxInspection.class);
		myFixture.configureByFiles("DataProtocol_empty.avdl");
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(myFixture.doHighlighting());

		// Note: because we're cutting out the text offsets, all error texts should be unique enough to be identified.
		// Luckily, the method returns highlights in the order they are in the file.

		assertOrderedEquals(highlight,
				Highlight.weakWarning("protocol", "Schema syntax available")
		);
	}

	public void testSchemaSyntaxInspectionWithoutNamespaceOrSchemaQuickFix() {
		myFixture.enableInspections(AvroIdlUseSchemaSyntaxInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("DataProtocol_empty.avdl");
		assertEquals(1, quickFixes.size());
		IntentionAction quickFix = quickFixes.get(0);
		assertEquals("Replace protocol with schema syntax", quickFix.getText());
		myFixture.launchAction(quickFix);
		myFixture.checkResultByFile("DataProtocolFixed_empty.avdl");
	}

	public void testSchemaSyntaxInspectionWithoutSchemaQuickFix() {
		myFixture.enableInspections(AvroIdlUseSchemaSyntaxInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("DataProtocol_ns.avdl");
		assertEquals(1, quickFixes.size());
		IntentionAction quickFix = quickFixes.get(0);
		assertEquals("Replace protocol with schema syntax", quickFix.getText());
		myFixture.launchAction(quickFix);
		myFixture.checkResultByFile("DataProtocolFixed_ns.avdl");
	}

	public void testSchemaSyntaxInspectionForRecordWithoutNamespace() {
		myFixture.enableInspections(AvroIdlUseSchemaSyntaxInspection.class);
		myFixture.configureByFiles("DataProtocol_record.avdl");
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(myFixture.doHighlighting());

		// Note: because we're cutting out the text offsets, all error texts should be unique enough to be identified.
		// Luckily, the method returns highlights in the order they are in the file.

		assertOrderedEquals(highlight,
				Highlight.weakWarning("protocol", "Schema syntax available")
		);
	}

	public void testSchemaSyntaxInspectionForRecordWithoutNamespaceQuickFix() {
		myFixture.enableInspections(AvroIdlUseSchemaSyntaxInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("DataProtocol_record.avdl");
		assertEquals(1, quickFixes.size());
		IntentionAction quickFix = quickFixes.get(0);
		assertEquals("Replace protocol with schema syntax", quickFix.getText());
		myFixture.launchAction(quickFix);
		myFixture.checkResultByFile("DataProtocolFixed_record.avdl");
	}

	public void testSchemaSyntaxInspectionForRecordWithNamespace() {
		myFixture.enableInspections(AvroIdlUseSchemaSyntaxInspection.class);
		myFixture.configureByFiles("DataProtocol_ns_record.avdl");
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(myFixture.doHighlighting());

		// Note: because we're cutting out the text offsets, all error texts should be unique enough to be identified.
		// Luckily, the method returns highlights in the order they are in the file.

		assertOrderedEquals(highlight,
				Highlight.weakWarning("protocol", "Schema syntax available")
		);
	}

	public void testSchemaSyntaxInspectionForRecordWithNamespaceQuickFix() {
		myFixture.enableInspections(AvroIdlUseSchemaSyntaxInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("DataProtocol_ns_record.avdl");
		assertEquals(1, quickFixes.size());
		IntentionAction quickFix = quickFixes.get(0);
		assertEquals("Replace protocol with schema syntax", quickFix.getText());
		myFixture.launchAction(quickFix);
		myFixture.checkResultByFile("DataProtocolFixed_ns_record.avdl");
	}

	public void testAvoidSchemaSyntaxInspection() {
		myFixture.enableInspections(AvroIdlAvoidSchemaSyntaxInspection.class);
		myFixture.configureByFiles("SchemaSyntax.avdl");
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(myFixture.doHighlighting());

		// Note: because we're cutting out the text offsets, all error texts should be unique enough to be identified.
		// Luckily, the method returns highlights in the order they are in the file.

		assertOrderedEquals(highlight,
				Highlight.error("namespace", "Use of schema syntax")
		);
	}

	public void testAvoidSchemaSyntaxInspectionQuickFix() {
		myFixture.enableInspections(AvroIdlAvoidSchemaSyntaxInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("SchemaSyntax.avdl");
		assertEquals(1, quickFixes.size());
		IntentionAction quickFix = quickFixes.get(0);
		assertEquals("Replace schema syntax with protocol", quickFix.getText());
		myFixture.launchAction(quickFix);
		myFixture.checkResultByFile("SchemaSyntaxFixed.avdl");
	}

	public void testAvoidSchemaSyntaxInspectionWithoutNamespace() {
		myFixture.enableInspections(AvroIdlAvoidSchemaSyntaxInspection.class);
		myFixture.configureByFiles("SchemaSyntaxWithoutNamespace.avdl");
		final List<Highlight> highlight = Highlight.fromHighlightInfoList(myFixture.doHighlighting());

		// Note: because we're cutting out the text offsets, all error texts should be unique enough to be identified.
		// Luckily, the method returns highlights in the order they are in the file.

		assertOrderedEquals(highlight,
				Highlight.error("record", "Use of schema syntax")
		);
	}

	public void testAvoidSchemaSyntaxInspectionWithoutNamespaceQuickFix() {
		myFixture.enableInspections(AvroIdlAvoidSchemaSyntaxInspection.class);
		final List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("SchemaSyntaxWithoutNamespace.avdl");
		assertEquals(1, quickFixes.size());
		IntentionAction quickFix = quickFixes.get(0);
		assertEquals("Replace schema syntax with protocol", quickFix.getText());
		myFixture.launchAction(quickFix);
		myFixture.checkResultByFile("SchemaSyntaxWithoutNamespaceFixed.avdl");
	}

	public void testMissingSchemaAddRecordQuickFix() {
		List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("MissingSchema.avdl");
		final Map<String, IntentionAction> quickFixesByName = quickFixes.stream()
				.collect(Collectors.toMap(IntentionAction::getText, a -> a,
						(a, b) -> {
							fail("Duplicate quick fixes (i.e. the same name occurs multiple times)");
							return null;
						}));
		assertEquals(3, quickFixesByName.size());
		assertContainsElements(quickFixesByName.keySet(), "Create record");
		myFixture.launchAction(quickFixesByName.get("Create record"));
		myFixture.checkResultByFile("MissingSchema_Record.avdl");
	}

	public void testMissingSchemaAddEnumQuickFix() {
		List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("MissingSchema.avdl");
		final Map<String, IntentionAction> quickFixesByName = quickFixes.stream()
				.collect(Collectors.toMap(IntentionAction::getText, a -> a,
						(a, b) -> {
							fail("Duplicate quick fixes (i.e. the same name occurs multiple times)");
							return null;
						}));
		assertEquals(3, quickFixesByName.size());
		assertContainsElements(quickFixesByName.keySet(), "Create enum");
		myFixture.launchAction(quickFixesByName.get("Create enum"));
		myFixture.checkResultByFile("MissingSchema_Enum.avdl");
	}

	public void testMissingSchemaAddFixedQuickFix() {
		List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("MissingSchema.avdl");
		final Map<String, IntentionAction> quickFixesByName = quickFixes.stream()
				.collect(Collectors.toMap(IntentionAction::getText, a -> a,
						(a, b) -> {
							fail("Duplicate quick fixes (i.e. the same name occurs multiple times)");
							return null;
						}));
		assertEquals(3, quickFixesByName.size());
		assertContainsElements(quickFixesByName.keySet(), "Create fixed");
		myFixture.launchAction(quickFixesByName.get("Create fixed"));
		myFixture.checkResultByFile("MissingSchema_Fixed.avdl");
	}

	public void testMissingSymbolInEmptyEnumQuickFix() {
		List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("MissingSymbolInEmptyEnum.avdl");
		assertEquals(1, quickFixes.size());
		IntentionAction quickFix = quickFixes.get(0);
		assertEquals("Create symbol", quickFix.getText());

		myFixture.launchAction(quickFix);
		myFixture.checkResultByFile("MissingSymbolInEmptyEnumFixed.avdl");
	}

	public void testMissingSymbolInEnumQuickFix() {
		List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("MissingSymbolInEnum.avdl");
		assertEquals(1, quickFixes.size());
		IntentionAction quickFix = quickFixes.get(0);
		assertEquals("Create symbol", quickFix.getText());

		myFixture.launchAction(quickFix);
		myFixture.checkResultByFile("MissingSymbolInEnumFixed.avdl");
	}

	public void testMissingErrorQuickFix() {
		List<IntentionAction> quickFixes = myFixture.getAllQuickFixes("MissingError.avdl");
		assertEquals(1, quickFixes.size());
		IntentionAction quickFix = quickFixes.get(0);
		assertEquals("Create error", quickFix.getText());

		myFixture.launchAction(quickFix);
		myFixture.checkResultByFile("MissingErrorFixed.avdl");
	}

	@SuppressWarnings("SameParameterValue")
	private record Highlight(HighlightSeverity severity, String text, String description) {
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
				result.add(new Highlight(highlightInfo.getSeverity(), highlightInfo.getText(),
						highlightInfo.getDescription()));
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
			Highlight highlight = (Highlight) o;
			return severity.equals(highlight.severity) &&
					text.equals(highlight.text) &&
					description.equals(highlight.description);
		}

		@Override
		public String toString() {
			return String.format("%s: %s -> %s", severity.getName().toLowerCase(), text, description);
		}
	}
}

