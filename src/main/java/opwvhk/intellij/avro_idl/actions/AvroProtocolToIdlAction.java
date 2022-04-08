package opwvhk.intellij.avro_idl.actions;

import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.AvroProtocolFileType;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Protocol;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static com.intellij.execution.ui.ConsoleViewContentType.*;

public class AvroProtocolToIdlAction extends ConversionActionBase {
	public AvroProtocolToIdlAction() {
		super("Convert to Avro IDL", AvroProtocolFileType.INSTANCE, AvroIdlFileType.INSTANCE);
	}

	@Override
	protected void convertFile(@NotNull Project project, @NotNull ConsoleView console, @NotNull VirtualFile file) {
		console.print("Converting " + AvroProtocolFileType.INSTANCE.getDisplayName() + " file ", SYSTEM_OUTPUT);
		console.printHyperlink(file.getName(), new OpenFileHyperlinkInfo(project, file, 0));
		console.print(" to an " + AvroIdlFileType.INSTANCE.getDisplayName() + " file\n", SYSTEM_OUTPUT);

		console.print("Parsing Avro Protocol file\n", NORMAL_OUTPUT);
		final Protocol protocol;
		try {
			protocol = Protocol.parse(file.toNioPath().toFile());
		} catch (AvroRuntimeException | IOException e) {
			console.print("Failed to parse AvroProtocol in " + file.getName() + "; please resolve errors first.\n", ERROR_OUTPUT);
			writeStackTrace(console, e);
			return;
		}

		console.print("Asking for file to write Avro IDL to...\n", NORMAL_OUTPUT);
		final VirtualFileWrapper wrapper = askForTargetFile(project, "Save as Avro IDL", "Choose the filename to save to",
			AvroIdlFileType.INSTANCE, file.getParent(), file.getNameWithoutExtension());
		if (wrapper != null) {
			final VirtualFile virtualFile = wrapper.getVirtualFile(true);
			if (virtualFile != null) {
				console.print("Writing Avro IDL to ", NORMAL_OUTPUT);
				console.printHyperlink(virtualFile.getName(), new OpenFileHyperlinkInfo(project, virtualFile, 0));
				console.print("\n", NORMAL_OUTPUT);
				WriteCommandAction.runWriteCommandAction(project, actionTitle, "AvroIDL", () -> {
					try (Writer writer = new OutputStreamWriter(virtualFile.getOutputStream(this))) {
						IdlUtils.writeIdlProtocol(writer, protocol);

						console.print("Wrote Avro IDL \"" + protocol.getName() + "\" to ", NORMAL_OUTPUT);
						console.printHyperlink(virtualFile.getName(), new OpenFileHyperlinkInfo(project, virtualFile, 0));
						console.print("\n", NORMAL_OUTPUT);
						FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, virtualFile), true);
					} catch (RuntimeException | IOException e) {
						console.print("Failed to write the Avro IDL to " + virtualFile.getName() + "\n" + e.getLocalizedMessage(), ERROR_OUTPUT);
						writeStackTrace(console, e);
					}
				});
			}
		}
	}
}

