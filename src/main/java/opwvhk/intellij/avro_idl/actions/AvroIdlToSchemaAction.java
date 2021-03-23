package opwvhk.intellij.avro_idl.actions;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.AvroSchemaFileType;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.compiler.idl.Idl;
import org.apache.avro.compiler.idl.ParseException;
import org.apache.avro.compiler.schema.SchemaVisitor;
import org.apache.avro.compiler.schema.SchemaVisitorAction;
import org.apache.avro.compiler.schema.Schemas;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

import static opwvhk.intellij.avro_idl.actions.AvroIdlNotifications.error;
import static opwvhk.intellij.avro_idl.actions.AvroIdlNotifications.info;

public class AvroIdlToSchemaAction extends ConversionActionBase {
	private static final Logger LOGGER = Logger.getInstance(AvroIdlToSchemaAction.class);

	public AvroIdlToSchemaAction() {
		super("Convert to AvroSchema", AvroIdlFileType.INSTANCE, AvroSchemaFileType.INSTANCE);
	}

	@Override
	protected void convertFile(@NotNull Project project, @NotNull VirtualFile file) {
		final List<Schema> schemasToWrite = new ArrayList<>();
		try {
			final Protocol protocol = new Idl(file.toNioPath().toFile()).CompilationUnit();

			// Make a list of all schemas, with the dependent schemas they (recursively) also define
			final List<List<Schema>> schemasWithDependencies = new ArrayList<>();
			for (Schema schema : protocol.getTypes()) {
				List<Schema> schemaAndDependencies = new ArrayList<>();
				schemasWithDependencies.add(Schemas.visit(schema, new SchemaVisitor<>() {
					@Override
					public SchemaVisitorAction visitTerminal(Schema terminal) {
						visit(terminal);
						return SchemaVisitorAction.CONTINUE;
					}

					@Override
					public SchemaVisitorAction visitNonTerminal(Schema nonTerminal) {
						visit(nonTerminal);
						return SchemaVisitorAction.CONTINUE;
					}

					private void visit(Schema s) {
						final Schema.Type type = s.getType();
						if (type == Schema.Type.RECORD || type == Schema.Type.ENUM || type == Schema.Type.FIXED) {
							schemaAndDependencies.add(s);
						}
					}

					@Override
					public SchemaVisitorAction afterVisitNonTerminal(Schema nonTerminal) {
						return SchemaVisitorAction.CONTINUE;
					}

					@Override
					public List<Schema> get() {
						return schemaAndDependencies;
					}
				}));
			}
			// Sort by length, largest first
			schemasWithDependencies.sort(Comparator.comparing((Function<List<Schema>, Integer>) List::size).reversed());

			// Take the minimum number of schemas that (recursively) define all types.
			final IdentityHashMap<Schema, Schema> missingSchemas = new IdentityHashMap<>();
			protocol.getTypes().forEach(s -> missingSchemas.put(s, s));
			for (List<Schema> schemaAndDependencies : schemasWithDependencies) {
				final Schema schema = schemaAndDependencies.get(0);
				if (missingSchemas.containsKey(schema)) {
					schemasToWrite.add(schema);
					missingSchemas.keySet().removeAll(schemaAndDependencies);
				}
			}
		} catch (IOException | ParseException e) {
			LOGGER.warn("Failed to parse Avro IDL in " + file.getPresentableName(), e);
			error(project, "Failed to parse Avro IDL in %s: please resolve errors first.\n" +
				"(the error is also written to the idea log)", file.getPresentableName());
			return;
		}
		try {
			if (schemasToWrite.size() == 1) {
				final byte[] schemaBytes = schemasToWrite.get(0).toString(true).getBytes(StandardCharsets.UTF_8);
				final VirtualFile destinationFile = writeSiblingFile(file,schemaBytes);
				info(project, "Converted Avro IDL in %s to AvroSchema in %s", file.getPresentableName(), destinationFile.getPresentableName());
			} else {
				List<String> resultingFileNames = new ArrayList<>();
				for (Schema schema : schemasToWrite) {
					final byte[] schemaBytes = schema.toString(true).getBytes(StandardCharsets.UTF_8);
					final VirtualFile destinationFile = writeSiblingFile(file, schema.getName(), schemaBytes);
					resultingFileNames.add(destinationFile.getPresentableName());
				}
				final StringJoiner stringJoiner = new StringJoiner(" ");
				resultingFileNames.forEach(stringJoiner::add);
				info(project, "Converted Avro IDL in %s to AvroSchema in %s", file.getPresentableName(), stringJoiner);
			}
		} catch (IOException e) {
			LOGGER.warn("Failed to write AvroProtocol", e);
			error(project, "Failed to write AvroProtocol. See the idea log for more details.");
		}
	}
}

