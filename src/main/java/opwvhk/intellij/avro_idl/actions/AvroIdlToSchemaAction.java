package opwvhk.intellij.avro_idl.actions;

import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.intellij.execution.ui.ConsoleViewContentType.*;

public class AvroIdlToSchemaAction extends ConversionActionBase {
	public AvroIdlToSchemaAction() {
		super("Convert to Avro Schema", AvroIdlFileType.INSTANCE, AvroSchemaFileType.INSTANCE);
	}

	@Override
	protected void convertFile(@NotNull Project project, @NotNull ConsoleView console, @NotNull VirtualFile file) {
		console.print("Converting " + AvroIdlFileType.INSTANCE.getDisplayName() + " file ", SYSTEM_OUTPUT);
		console.printHyperlink(file.getName(), new OpenFileHyperlinkInfo(project, file, 0));
		console.print(" to one or more " + AvroSchemaFileType.INSTANCE.getDisplayName() + " files\n", SYSTEM_OUTPUT);

		List<Schema> rootSchemas;
		try {
			console.print(String.format("Parsing IDL file %s\n", file.getName()), NORMAL_OUTPUT);
			final Protocol protocol = new Idl(file.toNioPath().toFile()).CompilationUnit();
			if (protocol.getTypes().isEmpty()) {
				console.print("No root schemas found: aborting.\n", NORMAL_OUTPUT);
				return;
			}

			// Find all root schemas (schemas not used by other schemas).
			console.print("Finding root schemas\n", NORMAL_OUTPUT);
			final String schemaNames = protocol.getTypes().stream().map(Schema::getName).collect(Collectors.joining(", "));
			console.print("Initial schemas from protocol: " + schemaNames + "\n", NORMAL_OUTPUT);

			// Use one instance to ensure we discover common roots
			final SchemaVisitor<List<Schema>> rootSchemaDiscoveringVisitor = new RootSchemaDiscoveringVisitor(console);
			protocol.getTypes().forEach(rootCandidate -> Schemas.visit(rootCandidate, rootSchemaDiscoveringVisitor));
			rootSchemas = rootSchemaDiscoveringVisitor.get();
			console.print("Root schemas: " + rootSchemas.stream().map(Schema::getName).collect(Collectors.joining(", ")) + "\n", NORMAL_OUTPUT);
		} catch (RuntimeException | ParseException | IOException e) {
			console.print(String.format("Failed to parse Avro IDL in %s: please resolve errors first.", file.getName()), ERROR_OUTPUT);
			writeStackTrace(console, e);
			return;
		}
		final VirtualFile destination;
		if (rootSchemas.size() == 1) {
			console.print("Asking for file to write Avro Schema to...\n", NORMAL_OUTPUT);
			final VirtualFileWrapper fileWrapper = askForTargetFile(project, "Save Avro Schema as", null, AvroSchemaFileType.INSTANCE,
				file.getParent(), rootSchemas.get(0).getName());
			if (fileWrapper != null) {
				destination = fileWrapper.getVirtualFile(true);
			} else {
				destination = null;
			}
		} else {
			console.print("Asking for path to (over)write Avro Schema files to...\n", NORMAL_OUTPUT);
			destination = askForTargetDirectory(project, null,
				"The root schemas will be stored in this directory, overwriting any existing files.", file.getParent());
		}
		if (destination == null) {
			return;
		}
		WriteCommandAction.runWriteCommandAction(project, actionTitle, "AvroIDL", () -> {
			try {
				if (rootSchemas.size() == 1) {
					writeSchema(project, console, destination, rootSchemas.get(0));
				} else {
					final String suffix = "." + findExtensionFor(AvroSchemaFileType.INSTANCE);
					for (Schema rootSchema : rootSchemas) {
						VirtualFile schemaFile = destination.findOrCreateChildData(this, rootSchema.getName() + suffix);
						writeSchema(project, console, schemaFile, rootSchema);
					}
				}
			} catch (RuntimeException | IOException e) {
				console.print("Failed to write the Schema(s) to " + destination.getName() + "\n", ERROR_OUTPUT);
				writeStackTrace(console, e);
			}
		});
	}

	private void writeSchema(@NotNull Project project, @NotNull ConsoleView console, VirtualFile destination, Schema schema) throws IOException {
		VfsUtil.saveText(destination, schema.toString(true));
		console.print("Wrote Avro Schema \"", NORMAL_OUTPUT);
		console.print(schema.getName(), NORMAL_OUTPUT);
		console.print("\" to ", NORMAL_OUTPUT);
		console.printHyperlink(destination.getName(), new OpenFileHyperlinkInfo(project, destination, 0));
		console.print("\n", NORMAL_OUTPUT);
	}

	private static class RootSchemaDiscoveringVisitor implements SchemaVisitor<List<Schema>> {
		private final List<Schema> rootSchemaCandidates;
		private final Set<Schema> usedNamedSchemas;
		private final Deque<Schema> stack;
		private final @NotNull ConsoleView console;

		public RootSchemaDiscoveringVisitor(@NotNull ConsoleView console) {
			this.console = console;
			rootSchemaCandidates = new ArrayList<>();
			usedNamedSchemas = new HashSet<>();
			stack = new ArrayDeque<>();
		}

		@Override
		public SchemaVisitorAction visitTerminal(Schema schema) {
			if (schema.getType() == Schema.Type.ENUM || schema.getType() == Schema.Type.FIXED) {
				// Enum & Fixed are named schemas, so these may also be roots
				// visitNonTerminal handles Record schemas
				return addNamedSchema(schema);
			}
			return SchemaVisitorAction.CONTINUE;
		}

		@Override
		public SchemaVisitorAction visitNonTerminal(Schema schema) {
			SchemaVisitorAction action = SchemaVisitorAction.CONTINUE;
			if (schema.getType() == Schema.Type.RECORD) {
				// Record is a named schema, so these may also be roots
				// visitTerminal handles Enum & Fixed schemas
				action = addNamedSchema(schema);
			}
			stack.push(schema);
			return action;
		}

		@Override
		public SchemaVisitorAction afterVisitNonTerminal(Schema nonTerminal) {
			stack.pop();
			return SchemaVisitorAction.CONTINUE;
		}

		private SchemaVisitorAction addNamedSchema(Schema schema) {
			if (stack.isEmpty()) {
				if (usedNamedSchemas.contains(schema)) {
					final String message = "Skipping schema " + schema.getName() + ": it was used within a previous root.\n";
					console.print(message, NORMAL_OUTPUT);
					return SchemaVisitorAction.TERMINATE;
				}
				rootSchemaCandidates.add(schema);
			} else if (usedNamedSchemas.add(schema)) {
				final String message = "Eliminating schema " + schema.getName() + ": it's used within " + stack.getLast().getName() + "\n";
				console.print(message, NORMAL_OUTPUT);
			}
			return SchemaVisitorAction.CONTINUE;
		}

		@Override
		public List<Schema> get() {
			rootSchemaCandidates.removeAll(usedNamedSchemas);
			return rootSchemaCandidates;
		}
	}
}

