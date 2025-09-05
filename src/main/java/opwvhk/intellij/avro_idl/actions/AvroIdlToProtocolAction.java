package opwvhk.intellij.avro_idl.actions;

import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.psi.PsiManager;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.AvroProtocolFileType;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlProtocolDeclaration;
import org.apache.avro.Protocol;
import org.apache.avro.idl.IdlFile;
import org.apache.avro.idl.IdlReader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

import static com.intellij.execution.ui.ConsoleViewContentType.*;
import static opwvhk.intellij.avro_idl.language.AvroIdlUtil.ifType;

public class AvroIdlToProtocolAction extends ConversionActionBase {
	public AvroIdlToProtocolAction() {
		super("Convert to Avro Protocol", AvroIdlFileType.INSTANCE, AvroProtocolFileType.INSTANCE);
	}

	@Override
	protected boolean canConvert(@NotNull VirtualFile file, Project project) {
		if (!super.canConvert(file, project)) {
			return false;
		}
		// Find out if the file contains a protocol
		PsiManager psiManager = PsiManager.getInstance(project);
		return ifType(psiManager.findFile(file), AvroIdlFile.class)
				.flatMap(avroIdlFile -> Stream.of(avroIdlFile.getChildren()))
				.flatMap(child -> ifType(child, AvroIdlProtocolDeclaration.class))
				.map(AvroIdlProtocolDeclaration::getFullName)
				.anyMatch(Objects::nonNull);
	}

	@Override
	protected void convertFile(@NotNull Project project, @NotNull ConsoleView console, @NotNull VirtualFile file) {
		console.print("Converting " + AvroIdlFileType.INSTANCE.getDisplayName() + " file ", SYSTEM_OUTPUT);
		console.printHyperlink(file.getName(), new OpenFileHyperlinkInfo(project, file, 0));
		console.print(" to an " + AvroProtocolFileType.INSTANCE.getDisplayName() + " file\n", SYSTEM_OUTPUT);

		console.print("Parsing IDL file\n", NORMAL_OUTPUT);
		final Protocol protocol;
		try {
			final IdlReader idlReader = new IdlReader();
			IdlFile idlFile = idlReader.parse(file.toNioPath());
			protocol = idlFile.getProtocol();
		} catch (RuntimeException | IOException e) {
			console.print("Failed to parse " + file.getName() + "; please resolve errors first.\n", ERROR_OUTPUT);
			writeStackTrace(console, e);
			return;
		}

		console.print("Asking for file to write Avro Protocol to...\n", NORMAL_OUTPUT);
		final VirtualFileWrapper wrapper = askForTargetFile(project, "Save as Avro Protocol",
				"Choose the filename to save to",
				AvroProtocolFileType.INSTANCE, file.getParent(), file.getNameWithoutExtension());
		if (wrapper != null) {
			final VirtualFile virtualFile = wrapper.getVirtualFile(true);
			if (virtualFile != null) {
				console.print("Writing Avro Protocol to ", NORMAL_OUTPUT);
				console.printHyperlink(virtualFile.getName(), new OpenFileHyperlinkInfo(project, virtualFile, 0));
				console.print("\n", NORMAL_OUTPUT);
				WriteCommandAction.runWriteCommandAction(project, actionTitle, "AvroIDL", () -> {
					try {
						VfsUtil.saveText(virtualFile, protocol.toString(true) + "\n");
						console.print("Wrote Avro Protocol \"" + protocol.getName() + "\" to ", NORMAL_OUTPUT);
						console.printHyperlink(virtualFile.getName(),
								new OpenFileHyperlinkInfo(project, virtualFile, 0));
						console.print("\n", NORMAL_OUTPUT);
					} catch (RuntimeException | IOException e) {
						console.print("Failed to write the AvroProtocol to " + virtualFile.getName() + "\n" +
								e.getLocalizedMessage(), ERROR_OUTPUT);
						writeStackTrace(console, e);
					}
				});
				if (virtualFile.exists()) {
					FileEditorManager.getInstance(project)
							.openTextEditor(new OpenFileDescriptor(project, virtualFile), true);
				}
			}
		}
	}
}

