package opwvhk.intellij.avro_idl.actions;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.avro.*;
import org.apache.avro.Schema.Field;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public final class IdlUtils {
	static final JsonFactory SCHEMA_FACTORY;
	private static final Function<Field, JsonNode> DEFAULT_VALUE;
	private static final Pattern NEWLINE_PATTERN = Pattern.compile("(?U)\\R");
	private static final String NEWLINE = System.lineSeparator();

	static {
		SCHEMA_FACTORY = getFieldValue(getField(Schema.class, "FACTORY"), null);

		java.lang.reflect.Field defaultValueField = getField(Field.class, "defaultValue");
		DEFAULT_VALUE = field -> getFieldValue(defaultValueField, field);
	}

	static java.lang.reflect.Field getField(Class<?> type, String name) {
		try {
			java.lang.reflect.Field field = type.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException("Programmer error", e);
		}
	}

	@SuppressWarnings("unchecked")
	static <T> T getFieldValue(java.lang.reflect.Field field, Object owner) {
		try {
			return (T) field.get(owner);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Programmer error", e);
		}
	}

	static Method getMethod(Class<?> type, String name, Class<?>... parameterTypes) {
		try {
			Method method = type.getDeclaredMethod(name, parameterTypes);
			method.setAccessible(true);
			return method;
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Programmer error", e);
		}
	}

	@SuppressWarnings("unchecked")
	static <T> T invokeMethod(Method method, Object owner, Object... parameters) {
		try {
			return (T) method.invoke(owner, parameters);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalStateException("Programmer error", e);
		}
	}

	private static final Method RESET_METHOD = getMethod(JsonWriteContext.class, "reset", Integer.TYPE);

	private static void resetContext(JsonStreamContext context) {
		invokeMethod(RESET_METHOD, context, 0);
	}

	private IdlUtils() {
		// Utility class: do not instantiate.
	}

	public static void writeIdlProtocol(Writer writer, Protocol protocol) throws IOException {
		final String protocolFullName = protocol.getName();
		final int lastDotPos = protocolFullName.lastIndexOf(".");
		final String protocolNameSpace;
		if (lastDotPos < 0) {
			protocolNameSpace = protocol.getNamespace();
		} else if (lastDotPos > 0) {
			protocolNameSpace = protocolFullName.substring(0, lastDotPos);
		} else {
			protocolNameSpace = null;
		}
		writeIdlProtocol(writer, protocol, protocolNameSpace, protocolFullName.substring(lastDotPos + 1), protocol.getTypes(), protocol.getMessages().values());
	}

	public static void writeIdlProtocol(Writer writer, String protocolNameSpace, String protocolName, Schema schema) throws IOException {
		final JsonProperties emptyProperties = Schema.create(Schema.Type.NULL);
		writeIdlProtocol(writer, emptyProperties, protocolNameSpace, protocolName, List.of(schema), Collections.emptyList());
	}

	public static void writeIdlProtocol(Writer writer, String protocolNameSpace, String protocolName, Collection<Schema> schemas) throws IOException {
		final JsonProperties emptyProperties = Schema.create(Schema.Type.NULL);
		writeIdlProtocol(writer, emptyProperties, protocolNameSpace, protocolName, schemas, Collections.emptyList());
	}

	public static void writeIdlProtocol(Writer writer, JsonProperties properties, String protocolNameSpace, String protocolName, Collection<Schema> schemas,
										Collection<Protocol.Message> messages) throws IOException {
		try (JsonGenerator jsonGen = SCHEMA_FACTORY.createGenerator(writer)) {
			if (protocolNameSpace != null) {
				writer.append("@namespace(\"").append(protocolNameSpace).append("\")").append(NEWLINE);
			}
			writeJsonProperties(properties, Collections.singleton("namespace"), writer, jsonGen, "");
			writer.append("protocol ").append(requireNonNull(protocolName)).append(" {").append(NEWLINE);

			Set<String> alreadyDeclared = new HashSet<>(4);
			Set<Schema> toDeclare = new LinkedHashSet<>(schemas);
			boolean first = true;
			while (!toDeclare.isEmpty()) {
				if (!first) {
					writer.append(NEWLINE);
				}
				Iterator<Schema> iterator = toDeclare.iterator();
				Schema schema = iterator.next();
				iterator.remove();
				writeSchema(schema, writer, jsonGen, protocolNameSpace, alreadyDeclared, toDeclare);
				first = false;
			}
			if (!schemas.isEmpty() && !messages.isEmpty()) {
				writer.append(NEWLINE);
			}
			for (Protocol.Message message : messages) {
				writeMessage(message, writer, jsonGen, protocolNameSpace, alreadyDeclared);
			}
			writer.append("}").append(NEWLINE);
		}
	}

	private static void writeSchema(Schema schema, Writer writer, JsonGenerator jsonGen, String protocolNameSpace, Set<String> alreadyDeclared,
									Set<Schema> toDeclare) throws IOException {
		Schema.Type type = schema.getType();
		writeSchemaAttributes(schema, writer, jsonGen);
		String namespace = schema.getNamespace(); // Fails for unnamed schema types (other types than record, enum & fixed)
		if (!Objects.equals(namespace, protocolNameSpace)) {
			writer.append("    @namespace(\"").append(namespace).append("\")").append(NEWLINE);
		}
		Set<String> schemaAliases = schema.getAliases();
		if (!schemaAliases.isEmpty()) {
			writer.append("    @aliases(");
			toJson(schemaAliases, jsonGen);
			jsonGen.flush();
			resetContext(jsonGen.getOutputContext());
			writer.append(")").append(NEWLINE);
		}
		if (type == Schema.Type.RECORD) {
			writer.append("    ").append(schema.isError() ? "error" : "record").append(" ").append(schema.getName()).append(" {").append(NEWLINE);
			alreadyDeclared.add(schema.getFullName());
			for (Field field : schema.getFields()) {
				writeField(schema.getNamespace(), field, writer, jsonGen, alreadyDeclared, toDeclare, true);
				writer.append(";").append(NEWLINE);
			}
			writer.append("    }").append(NEWLINE);
		} else if (type == Schema.Type.ENUM) {
			writer.append("    enum ").append(schema.getName()).append(" {");
			alreadyDeclared.add(schema.getFullName());
			Iterator<String> i = schema.getEnumSymbols().iterator();
			if (i.hasNext()) {
				writer.append(i.next());
				while (i.hasNext()) {
					writer.append(", ");
					writer.append(i.next());
				}
			} else {
				throw new AvroRuntimeException("Enum schema must have at least a symbol " + schema);
			}
			writer.append("}").append(NEWLINE);
		} else /* (type == Schema.Type.FIXED) */ {
			writer.append("    fixed ").append(schema.getName()).append('(')
				.append(Integer.toString(schema.getFixedSize())).append(");").append(NEWLINE);
			alreadyDeclared.add(schema.getFullName());
		}
	}

	private static void writeField(String namespace, Field field, Writer writer, JsonGenerator jsonGen, Set<String> alreadyDeclared, Set<Schema> toDeclare,
								   boolean indentField) throws IOException {
		// Note: indentField should be true if any field of the containing record has documentation
		writeDocumentation(writer, "        ", field.doc());
		if (indentField) {
			writer.append("        ");
		}
		writeFieldSchema(field.schema(), writer, jsonGen, alreadyDeclared, toDeclare, namespace);
		writer.append(' ');
		Set<String> fieldAliases = field.aliases();
		if (!fieldAliases.isEmpty()) {
			writer.append("@aliases(");
			toJson(fieldAliases, jsonGen);
			jsonGen.flush();
			resetContext(jsonGen.getOutputContext());
			writer.append(") ");
		}
		Field.Order order = field.order();
		if (order != Field.Order.ASCENDING) {
			writer.append("@order(\"").append(order.name()).append("\") ");
		}
		writeJsonProperties(field, writer, jsonGen, null);
		writer.append(field.name());
		JsonNode defaultValue = DEFAULT_VALUE.apply(field);
		if (defaultValue != null) {
			writer.append(" = ");
			toJson(field.defaultVal(), jsonGen);
			jsonGen.flush();
			resetContext(jsonGen.getOutputContext());
		}
	}

	private static void writeDocumentation(Writer writer, String indent, String doc) throws IOException {
		if (doc == null || doc.isBlank()) {
			return;
		}
		writer.append(formatDocumentationComment(indent, doc));
	}

	private static String formatDocumentationComment(String indent, String doc) {
		assert !doc.isBlank() : "There must be documentation to format!";

		StringBuilder buffer = new StringBuilder();
		buffer.append(indent).append("/** ");
		boolean foundMatch = false;
		final Matcher matcher = NEWLINE_PATTERN.matcher(doc);
		final String newlinePlusIndent = NEWLINE + indent + " * ";
		while (matcher.find()) {
			if (!foundMatch) {
				buffer.append(newlinePlusIndent);
				foundMatch = true;
			}
			matcher.appendReplacement(buffer, newlinePlusIndent);
		}
		if (foundMatch) {
			matcher.appendTail(buffer);
			buffer.append(NEWLINE).append(indent).append(" */").append(NEWLINE);
		} else {
			buffer.append(doc).append(" */").append(NEWLINE);
		}
		return buffer.toString();
	}

	private static void writeFieldSchema(Schema schema, Writer writer, JsonGenerator jsonGen, Set<String> alreadyDeclared, Set<Schema> toDeclare,
										 String recordNameSpace) throws IOException {
		Schema.Type type = schema.getType();
		if (type == Schema.Type.RECORD || type == Schema.Type.ENUM || type == Schema.Type.FIXED) {
			if (Objects.equals(recordNameSpace, schema.getNamespace())) {
				writer.append(schema.getName());
			} else {
				writer.append(schema.getFullName());
			}
			if (!alreadyDeclared.contains(schema.getFullName())) {
				toDeclare.add(schema);
			}
		} else if (type == Schema.Type.ARRAY) {
			writeJsonProperties(schema, writer, jsonGen, null);
			writer.append("array<");
			writeFieldSchema(schema.getElementType(), writer, jsonGen, alreadyDeclared, toDeclare, recordNameSpace);
			writer.append('>');
		} else if (type == Schema.Type.MAP) {
			writeJsonProperties(schema, writer, jsonGen, null);
			writer.append("map<");
			writeFieldSchema(schema.getValueType(), writer, jsonGen, alreadyDeclared, toDeclare, recordNameSpace);
			writer.append('>');
		} else if (type == Schema.Type.UNION) {
			// Note: unions cannot have properties
			List<Schema> types = schema.getTypes();
			if (schema.isNullable() && types.size() == 2) {
				Schema nonNullSchema = !types.get(0).isNullable() ? types.get(0) : types.get(1);
				writeFieldSchema(nonNullSchema, writer, jsonGen, alreadyDeclared, toDeclare, recordNameSpace);
				writer.append('?');
			} else {
				writer.append("union{");
				Iterator<Schema> iterator = types.iterator();
				if (iterator.hasNext()) {
					writeFieldSchema(iterator.next(), writer, jsonGen, alreadyDeclared, toDeclare, recordNameSpace);
					while (iterator.hasNext()) {
						writer.append(", ");
						writeFieldSchema(iterator.next(), writer, jsonGen, alreadyDeclared, toDeclare, recordNameSpace);
					}
				} else {
					throw new AvroRuntimeException("Union schemas must have member types " + schema);
				}
				writer.append('}');
			}
		} else {
			Set<String> propertiesToSkip;
			String typeName;
			if (schema.getLogicalType() == null) {
				propertiesToSkip = Collections.emptySet();
				typeName = schema.getName();
			} else {
				String logicalName = schema.getLogicalType().getName();
				switch (logicalName) {
					case "date":
					case "time-millis":
					case "timestamp-millis":
						propertiesToSkip = Collections.singleton("logicalType");
						typeName = logicalName.replace("-millis", "_ms");
						break;
					case "decimal":
						propertiesToSkip = Set.of("logicalType", "precision", "scale");
						LogicalTypes.Decimal decimal = (LogicalTypes.Decimal) schema.getLogicalType();
						typeName = String.format("decimal(%d,%d)", decimal.getPrecision(), decimal.getScale());
						break;
					default:
						propertiesToSkip = Collections.emptySet();
						typeName = schema.getName();
						break;
				}
			}
			writeJsonProperties(schema, propertiesToSkip, writer, jsonGen, null);
			writer.append(typeName);
		}
	}

	private static void writeSchemaAttributes(Schema schema, Writer writer, JsonGenerator jsonGen) throws IOException {
		writeDocumentation(writer, "    ", schema.getDoc());
		writeJsonProperties(schema, writer, jsonGen, "    ");
	}

	private static void writeJsonProperties(JsonProperties props, Writer writer, JsonGenerator jsonGen, String indent) throws IOException {
		writeJsonProperties(props, Collections.emptySet(), writer, jsonGen, indent);
	}

	private static void writeJsonProperties(JsonProperties props, Set<String> propertiesToSkip, Writer writer, JsonGenerator jsonGen, String indent)
		throws IOException {
		Map<String, Object> objectProps = props.getObjectProps();
		for (Map.Entry<String, Object> entry : objectProps.entrySet()) {
			if (propertiesToSkip.contains(entry.getKey())) {
				continue;
			}
			if (indent != null) {
				writer.append(indent);
			}
			writer.append('@').append(entry.getKey()).append('(');
			toJson(entry.getValue(), jsonGen);
			jsonGen.flush();
			resetContext(jsonGen.getOutputContext());
			writer.append(')').append(indent == null ? ' ' : '\n');
		}
	}

	static void toJson(Object datum, JsonGenerator generator) throws IOException {
		if (datum == JsonProperties.NULL_VALUE) { // null
			generator.writeNull();
		} else if (datum instanceof Map) { // record, map
			generator.writeStartObject();
			for (Map.Entry<?,?> entry : ((Map<?,?>)datum).entrySet()) {
				generator.writeFieldName(entry.getKey().toString());
				toJson(entry.getValue(), generator);
			}
			generator.writeEndObject();
		} else if (datum instanceof Collection) { // array
			generator.writeStartArray();
			for (Object element : (Collection<?>) datum) {
				toJson(element, generator);
			}
			generator.writeEndArray();
		} else if (datum instanceof byte[]) { // bytes, fixed
			generator.writeString(new String((byte[]) datum, StandardCharsets.ISO_8859_1));
		} else if (datum instanceof CharSequence || datum instanceof Enum<?>) { // string, enum
			generator.writeString(datum.toString());
		} else if (datum instanceof Double) { // double
			generator.writeNumber((Double) datum);
		} else if (datum instanceof Float) { // float
			generator.writeNumber((Float) datum);
		} else if (datum instanceof Long) { // long
			generator.writeNumber((Long) datum);
		} else if (datum instanceof Integer) { // int
			generator.writeNumber((Integer) datum);
		} else if (datum instanceof Boolean) { // boolean
			generator.writeBoolean((Boolean) datum);
		} else {
			throw new AvroRuntimeException("Unknown datum class: " + datum.getClass());
		}
	}


	private static void writeMessage(Protocol.Message message, Writer writer, JsonGenerator jsonGen, String protocolNameSpace, Set<String> alreadyDeclared)
		throws IOException {
		writeMessageAttributes(message, writer, jsonGen);
		final Set<Schema> toDeclare = Collections.unmodifiableSet(new HashSet<>()); // Crash if a type hasn't been declared yet.
		writer.append("    ");
		writeFieldSchema(message.getResponse(), writer, jsonGen, alreadyDeclared, toDeclare, protocolNameSpace);
		writer.append(' ');
		writer.append(message.getName());

		Schema request = message.getRequest(); // MUST be a record type
		boolean indentParameters = request.getFields().stream().anyMatch(field -> field.doc() != null && !field.doc().isBlank());
		writer.append('(');
		if (indentParameters) {
			writer.append("\n");
		}

		boolean first = true;
		for (Field field : request.getFields()) {
			if (first) {
				first = false;
			} else if (indentParameters) {
				writer.append(",\n");
			} else {
				writer.append(", ");
			}
			writeField(protocolNameSpace, field, writer, jsonGen, alreadyDeclared, toDeclare, indentParameters);
		}
		if (indentParameters) {
			writer.append("\n    ");
		}
		writer.append(')');

		if (message.isOneWay()) {
			writer.append(" oneway;\n");
		} else {
			first = true;
			// MUST be a union of error types
			for (Schema error : message.getErrors().getTypes()) {
				if (error.getType() == Schema.Type.STRING) {
					continue; // Skip system error type
				}
				if (first) {
					first = false;
					writer.append(" throws ");
				} else {
					writer.append(", ");
				}
				if (Objects.equals(protocolNameSpace, error.getNamespace())) {
					writer.append(error.getName());
				} else {
					writer.append(error.getFullName());
				}
			}
			writer.append(";\n");
		}
	}

	private static void writeMessageAttributes(Protocol.Message message, Writer writer, JsonGenerator jsonGen) throws IOException {
		writeDocumentation(writer, "    ", message.getDoc());
		writeJsonProperties(message, writer, jsonGen, "    ");
	}
}
