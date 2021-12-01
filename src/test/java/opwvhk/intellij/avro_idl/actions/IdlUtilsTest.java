package opwvhk.intellij.avro_idl.actions;

import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.JsonProperties;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.compiler.idl.Idl;
import org.apache.avro.compiler.idl.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IdlUtilsTest {
	@Test
	public void idlUtilsUtilitiesThrowRuntimeExceptions() {
		assertThatThrownBy(() -> IdlUtils.getField(Object.class, "noSuchField"))
			.isInstanceOf(IllegalStateException.class).hasMessage("Programmer error");
		assertThatThrownBy(() -> IdlUtils.getFieldValue(String.class.getDeclaredField("value"), "anything"))
			.isInstanceOf(IllegalStateException.class).hasMessage("Programmer error");

		assertThatThrownBy(() -> IdlUtils.getMethod(Object.class, "noSuchMethod"))
			.isInstanceOf(IllegalStateException.class).hasMessage("Programmer error");
		assertThatThrownBy(() -> {
			Object foo = IdlUtils.invokeMethod(Object.class.getDeclaredMethod("clone"), new Object());
			System.err.println(foo);
		}).isInstanceOf(IllegalStateException.class).hasMessage("Programmer error");
	}

	@Test
	public void validateHappyFlowForProtocol() throws ParseException, IOException {
		final String resourceAsString = getResourceAsString("idl_utils_test_protocol.avdl");
		Protocol protocol = new Idl(new StringReader(resourceAsString)).CompilationUnit();
		// Write as JSON and parse again to handle logical types correctly.
		protocol = Protocol.parse(protocol.toString());
		Schema newMessageSchema = protocol.getType("naming.NewMessage");

		StringWriter buffer = new StringWriter();
		IdlUtils.writeIdlProtocol(buffer, protocol);

		assertThat(buffer.toString()).isEqualTo(resourceAsString);
	}

	private String getResourceAsString(String name) throws IOException {
		StringWriter schemaBuffer = new StringWriter();
		try (InputStreamReader reader = new InputStreamReader(requireNonNull(getClass().getResourceAsStream(name)))) {
			char[] buf = new char[1024];
			int charsRead;
			while ((charsRead = reader.read(buf)) > -1) {
				schemaBuffer.write(buf, 0, charsRead);
			}
		}
		return schemaBuffer.toString();
	}

	@Test
	public void validateHappyFlowForSingleSchema() throws ParseException, IOException {
		final String resourceAsString = getResourceAsString("idl_utils_test_schema.avdl");
		Protocol protocol = new Idl(new StringReader(resourceAsString)).CompilationUnit();
		// Write as JSON and parse again to handle logical types correctly.
		protocol = Protocol.parse(protocol.toString());
		Schema newMessageSchema = protocol.getType("naming.NewMessage");

		StringWriter buffer = new StringWriter();
		IdlUtils.writeIdlProtocol(buffer, "naming", "HappyFlow", newMessageSchema);

		assertThat(buffer.toString()).isEqualTo(resourceAsString);
	}

	@Test
	public void cannotWriteUnnamedTypes() {
		assertThatThrownBy(() -> IdlUtils.writeIdlProtocol(new StringWriter(), "naming", "Error",
			Schema.create(Schema.Type.STRING))).isInstanceOf(AvroRuntimeException.class);
	}

	@Test
	public void cannotWriteEmptyEnums() {
		assertThatThrownBy(() -> IdlUtils.writeIdlProtocol(new StringWriter(), "naming", "Error",
			Schema.createEnum("Single", null, "naming", emptyList()))).isInstanceOf(AvroRuntimeException.class);
	}

	@Test
	public void cannotWriteEmptyUnionTypes() {
		assertThatThrownBy(() -> IdlUtils.writeIdlProtocol(new StringWriter(), "naming", "Error",
			Schema.createRecord("Single", null, "naming", false, singletonList(
				new Schema.Field("field", Schema.createUnion())
			)))).isInstanceOf(AvroRuntimeException.class);
	}

	@Test
	public void validateNullToJson() throws IOException {
		assertThat(callToJson(JsonProperties.NULL_VALUE)).isEqualTo("null");
	}

	@Test
	public void validateMapToJson() throws IOException {
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("key", "name");
		data.put("value", 81763);
		assertThat(callToJson(data)).isEqualTo("{\"key\":\"name\",\"value\":81763}");
	}

	@Test
	public void validateCollectionToJson() throws IOException {
		assertThat(callToJson(Arrays.asList(123, "abc"))).isEqualTo("[123,\"abc\"]");
	}

	@Test
	public void validateBytesToJson() throws IOException {
		assertThat(callToJson("getalletjes".getBytes(StandardCharsets.US_ASCII))).isEqualTo("\"getalletjes\"");
	}

	@Test
	public void validateStringToJson() throws IOException {
		assertThat(callToJson("foo")).isEqualTo("\"foo\"");
	}

	@Test
	public void validateEnumToJson() throws IOException {
		assertThat(callToJson(SingleValue.FILE_NOT_FOUND)).isEqualTo("\"FILE_NOT_FOUND\"");
	}

	@Test
	public void validateDoubleToJson() throws IOException {
		assertThat(callToJson(25_000.025)).isEqualTo("25000.025");
	}

	@Test
	public void validateFloatToJson() throws IOException {
		assertThat(callToJson(15_000.002f)).isEqualTo("15000.002");
	}

	@Test
	public void validateLongToJson() throws IOException {
		assertThat(callToJson(7254378234L)).isEqualTo("7254378234");
	}

	@Test
	public void validateIntegerToJson() throws IOException {
		assertThat(callToJson(123)).isEqualTo("123");
	}

	@Test
	public void validateBooleanToJson() throws IOException {
		assertThat(callToJson(true)).isEqualTo("true");
	}

	@Test
	public void validateUnknownCannotBeWrittenAsJson() {
		assertThatThrownBy(() -> callToJson(new Object())).isInstanceOf(AvroRuntimeException.class);
	}

	private String callToJson(Object datum) throws IOException {
		StringWriter buffer = new StringWriter();
		try (JsonGenerator generator = IdlUtils.SCHEMA_FACTORY.createGenerator(buffer)) {
			IdlUtils.toJson(datum, generator);
		}
		return buffer.toString();
	}

	private enum SingleValue {
		FILE_NOT_FOUND
	}
}
