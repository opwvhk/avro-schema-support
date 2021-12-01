package opwvhk.intellij.avro_idl;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AvroIdlCodeCompletionsTest extends LightJavaCodeInsightFixtureTestCase {

	@Override
	protected String getTestDataPath() {
		return "src/test/testData/codeCompletions";
	}

	public void testCompletionsInEmptyFile() {
		assertThat(getBasicCompletions("EmptyFile.avdl")).containsExactlyInAnyOrder("@namespace(\"\")", "protocol ");
	}

	public void testCompletionsForAnnotationInProtocol() {
		assertThat(getBasicCompletions("AnnotationInProtocol.avdl")).containsExactlyInAnyOrder("namespace(\"\")");
	}

	public void testCompletionsForProtocolBody() {
		assertThat(getBasicCompletions("ProtocolBody.avdl")).containsExactlyInAnyOrder(
			"import ", "enum ", "record ", "error ", "fixed ", "void ",
			"boolean ", "bytes ", "int ", "string ", "float ", "double ", "long ",
			"null ",
			"date ", "time_ms ", "local_timestamp_ms ", "timestamp_ms ", "decimal(",
			"union {", "array<", "map<",
			"Rec1", "other.namespace.Enum"
		);
	}

	public void testCompletionsForEnumDefaults() {
		assertThat(getBasicCompletions("EnumDefaults.avdl")).containsExactlyInAnyOrder("TRUE", "FALSE", "FILE_NOT_FOUND");
	}

	public void testCompletionsForRecordBody() {
		assertThat(getBasicCompletions("RecordBody.avdl")).containsExactlyInAnyOrder(
			"boolean ", "bytes ", "int ", "string ", "float ", "double ", "long ",
			"null ",
			"date ", "time_ms ", "local_timestamp_ms ", "timestamp_ms ", "decimal(",
			"union {", "array<", "map<",
			"Rec1", "Rec2", "other.namespace.Enum"
		);
	}

	public void testCompletionsForImportTypes() {
		assertThat(getBasicCompletions("ImportTypes.avdl")).containsExactlyInAnyOrder("idl ", "protocol ", "schema ");
	}

	public void testCompletionsForNamedSchemaAnnotations() {
		assertThat(getBasicCompletions("NamedSchemaAnnotations.avdl")).containsExactlyInAnyOrder("namespace(\"\")", "aliases([\"\"])", "logicalType(\"\")");
	}

	public void testCompletionsForUnnamedSchemaAnnotations() {
		assertThat(getBasicCompletions("UnnamedSchemaAnnotations.avdl")).containsExactlyInAnyOrder("logicalType(\"\")");
	}

	public void testCompletionsForFieldAnnotations() {
		assertThat(getBasicCompletions("FieldAnnotations.avdl")).containsExactlyInAnyOrder("order(\"\")", "aliases([\"\"])");
	}

	public void testCompletionsForOrderAnnotationStringsOnIncompleteFields() {
		assertThat(getBasicCompletions("OrderAnnotationStringsOnIncompleteFields.avdl")).containsExactlyInAnyOrder("\"ascending\"", "\"descending\"", "\"ignore\"");
	}

	public void testCompletionsForOrderAnnotationStrings() {
		assertThat(getBasicCompletions("OrderAnnotationStrings.avdl")).containsExactlyInAnyOrder("\"ascending\"", "\"descending\"", "\"ignore\"");
	}

	public void testCompletionsForOrderAnnotationValuesOnIncompleteFields() {
		assertThat(getBasicCompletions("OrderAnnotationValuesOnIncompleteFields.avdl")).containsExactlyInAnyOrder("ascending", "descending", "ignore");
	}

	public void testCompletionsForOrderAnnotationValues() {
		assertThat(getBasicCompletions("OrderAnnotationValues.avdl")).containsExactlyInAnyOrder("ascending", "descending", "ignore");
	}

	public void testCompletionsForFirstFormalParameter() {
		assertThat(getBasicCompletions("FirstFormalParameter.avdl")).containsExactlyInAnyOrder(
			"boolean ", "bytes ", "int ", "string ", "float ", "double ", "long ",
			"null ",
			"date ", "time_ms ", "local_timestamp_ms ", "timestamp_ms ", "decimal(",
			"union {", "array<", "map<",
			"Rec1", "other.namespace.Enum"
		);
	}

	public void testCompletionsForOtherFormalParameter() {
		assertThat(getBasicCompletions("OtherFormalParameter.avdl")).containsExactlyInAnyOrder(
			"boolean ", "bytes ", "int ", "string ", "float ", "double ", "long ",
			"null ",
			"date ", "time_ms ", "local_timestamp_ms ", "timestamp_ms ", "decimal(",
			"union {", "array<", "map<",
			"Rec1", "other.namespace.Enum"
		);
	}

	public void testCompletionsForMessageAttributes() {
		assertThat(getBasicCompletions("MessageAttributesKeywords.avdl")).containsExactlyInAnyOrder("oneway", "throws ");
	}

	public void testCompletionsForMessageThrowsClause() {
		assertThat(getBasicCompletions("MessageThrowsClause.avdl")).containsExactlyInAnyOrder("Failure");
	}

	private @Nullable List<String> getBasicCompletions(String... filePaths) {
		myFixture.configureByFiles(filePaths);
		myFixture.complete(CompletionType.BASIC, 1);
		return myFixture.getLookupElementStrings();
	}
}

