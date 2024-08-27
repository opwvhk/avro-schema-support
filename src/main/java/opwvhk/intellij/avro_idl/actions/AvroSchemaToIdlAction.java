package opwvhk.intellij.avro_idl.actions;

import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.AvroSchemaFileType;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.AvroTypeException;
import org.apache.avro.Schema;
import org.apache.avro.SchemaParser;
import org.apache.avro.idl.IdlUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.execution.ui.ConsoleViewContentType.*;

public class AvroSchemaToIdlAction extends ConversionActionBase {
	public AvroSchemaToIdlAction() {
		super("Convert to Avro IDL", AvroSchemaFileType.INSTANCE, AvroIdlFileType.INSTANCE);
	}

	protected void convertFiles(@NotNull Project project, @NotNull ConsoleView console,
	                            @NotNull List<VirtualFile> files) {
		console.print("Converting " + files.size() + " " + AvroSchemaFileType.INSTANCE.getDisplayName() + " file(s) ",
				SYSTEM_OUTPUT);
		console.print("to a single " + AvroIdlFileType.INSTANCE.getDisplayName() + " file\n", SYSTEM_OUTPUT);

		final SchemaParser parser = new SchemaParser();
		for (VirtualFile file : files) {
			try {
				console.print("Parsing ", SYSTEM_OUTPUT);
				console.printHyperlink(file.getName(), new OpenFileHyperlinkInfo(project, file, 0));
				console.print("\n", SYSTEM_OUTPUT);
				parser.parse(file.toNioPath());
			} catch (AvroRuntimeException | IOException e) {
				console.print("Failed to parse Avro schema in " + file.getName() + "\n", ERROR_OUTPUT);
				writeStackTrace(console, e);
				return;
			}
		}
		final List<Schema> schemas;
		try {
			schemas = new ArrayList<>(parser.getParsedNamedSchemas());
		} catch (AvroTypeException e) {
			console.print(e.getMessage(), ERROR_OUTPUT);
			console.print("\n\n", ERROR_OUTPUT);
			console.print("This error usually means that the selected files reference schemata that they do " +
					"not define. Though the Avro parser supports parsing incomplete schemata to overcome the lack of " +
					"imports in .avsc/.avpr files, .avdl files are expected to either define or import all schemata " +
					"they use. To fix this error, please select a complete set of schemata.\n", ERROR_OUTPUT);
			return;
		}

		console.print("Asking for file to write Avro IDL to...\n", NORMAL_OUTPUT);
		final VirtualFileWrapper wrapper = askForTargetFile(project, "Save as Avro IDL",
				"Choose the filename to save to",
				AvroIdlFileType.INSTANCE, files.get(0).getParent(), files.get(0).getNameWithoutExtension());
		if (wrapper != null) {
			final VirtualFile virtualFile = wrapper.getVirtualFile(true);
			if (virtualFile != null) {
				console.print("Writing Avro IDL to ", NORMAL_OUTPUT);
				console.printHyperlink(virtualFile.getName(), new OpenFileHyperlinkInfo(project, virtualFile, 0));
				console.print("\n", NORMAL_OUTPUT);
				WriteCommandAction.runWriteCommandAction(project, actionTitle, "AvroIDL", () -> {
					try (Writer writer = new OutputStreamWriter(virtualFile.getOutputStream(this))) {
						final String protocolName = virtualFile.getNameWithoutExtension();
						final String namespace = schemas.get(0)
								.getNamespace(); // Assume the first schema has the correct namespace.
						IdlUtils.writeIdlSchemas(writer, namespace, schemas);

						console.print("Wrote Avro IDL \"", NORMAL_OUTPUT);
						console.print(protocolName, NORMAL_OUTPUT);
						console.print("\" to ", NORMAL_OUTPUT);
						console.printHyperlink(virtualFile.getName(),
								new OpenFileHyperlinkInfo(project, virtualFile, 0));
						console.print("\n", NORMAL_OUTPUT);
						FileEditorManager.getInstance(project)
								.openTextEditor(new OpenFileDescriptor(project, virtualFile), true);
					} catch (RuntimeException | IOException e) {
						console.print("Failed to write the Avro IDL to ", ERROR_OUTPUT);
						console.print(virtualFile.getName(), ERROR_OUTPUT);
						console.print("\n", ERROR_OUTPUT);
						console.print(e.getLocalizedMessage(), ERROR_OUTPUT);
						console.print("\n", ERROR_OUTPUT);
						writeStackTrace(console, e);
					}
				});
			}
		}

		console.print("\nAction complete.\n", ConsoleViewContentType.SYSTEM_OUTPUT);
	}
}

