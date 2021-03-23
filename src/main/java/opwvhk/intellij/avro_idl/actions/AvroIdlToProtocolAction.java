package opwvhk.intellij.avro_idl.actions;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.AvroProtocolFileType;
import org.apache.avro.Protocol;
import org.apache.avro.compiler.idl.Idl;
import org.apache.avro.compiler.idl.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static opwvhk.intellij.avro_idl.actions.AvroIdlNotifications.error;
import static opwvhk.intellij.avro_idl.actions.AvroIdlNotifications.info;

public class AvroIdlToProtocolAction extends ConversionActionBase {
	private static final Logger LOGGER = Logger.getInstance(AvroIdlToProtocolAction.class);

	public AvroIdlToProtocolAction() {
		super("Convert to AvroProtocol", AvroIdlFileType.INSTANCE, AvroProtocolFileType.INSTANCE);
	}

	@Override
	protected void convertFile(@NotNull Project project, @NotNull VirtualFile file) {
		final String protocolAsJson;
		try {
			final Protocol protocol = new Idl(file.toNioPath().toFile()).CompilationUnit();
			protocolAsJson = protocol.toString(true);
		} catch (IOException | ParseException e) {
			LOGGER.warn("Failed to parse Avro IDL in " + file.getPresentableName(), e);
			error(project, "Failed to parse Avro IDL in %s: please resolve errors first.\n" +
				"(the error is also written to the idea log)", file.getPresentableName());
			return;
		}
		try {
			final byte[] jsonBytes = protocolAsJson.getBytes(StandardCharsets.UTF_8);
			final VirtualFile destinationFile = writeSiblingFile(file, jsonBytes);
			info(project, "Converted Avro IDL in %s to AvroProtocol in %s", file.getPresentableName(), destinationFile.getPresentableName());
		} catch (IOException e) {
			LOGGER.warn("Failed to write AvroProtocol", e);
			error(project, "Failed to write AvroProtocol. See the idea log for more details.");
		}
	}
}

