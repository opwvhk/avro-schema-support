package opwvhk.intellij.avro_idl.actions;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.AvroProtocolFileType;
import org.apache.avro.Protocol;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static opwvhk.intellij.avro_idl.actions.AvroIdlNotifications.error;
import static opwvhk.intellij.avro_idl.actions.AvroIdlNotifications.info;

public class AvroProtocolToIdlAction extends ConversionActionBase {
	private static final Logger LOGGER = Logger.getInstance(AvroProtocolToIdlAction.class);

	public AvroProtocolToIdlAction() {
		super("Convert to Avro IDL", AvroProtocolFileType.INSTANCE, AvroIdlFileType.INSTANCE);
	}

	@Override
	protected void convertFile(@NotNull Project project, @NotNull VirtualFile file) {
		final Protocol protocol;
		try {
			protocol = Protocol.parse(file.toNioPath().toFile());
		} catch (IOException e) {
			LOGGER.warn("Failed to parse AvroProtocol in " + file.getPresentableName(), e);
			error(project, "Failed to parse AvroProtocol in %s: please resolve errors first.\n" +
				"(the error is also written to the idea log)", file.getPresentableName());
			return;
		}
		try {
			final VirtualFile destinationFile = createSiblingFile(file);
			try (Writer writer = new OutputStreamWriter(destinationFile.getOutputStream(this))) {
				IdlUtils.writeIdlProtocol(writer, protocol);
			}
			info(project, "Converted AvroProtocol in %s to Avro IDL in %s", file.getPresentableName(), destinationFile.getPresentableName());
		} catch (IOException e) {
			LOGGER.warn("Failed to write Avro IDL", e);
			error(project, "Failed to write Avro IDL. See the idea log for more details.");
		}
	}
}

