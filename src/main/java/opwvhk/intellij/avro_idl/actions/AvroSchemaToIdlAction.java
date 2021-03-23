package opwvhk.intellij.avro_idl.actions;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import opwvhk.intellij.avro_idl.AvroIdlFileType;
import opwvhk.intellij.avro_idl.AvroSchemaFileType;
import org.apache.avro.Schema;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static opwvhk.intellij.avro_idl.actions.AvroIdlNotifications.error;
import static opwvhk.intellij.avro_idl.actions.AvroIdlNotifications.info;

public class AvroSchemaToIdlAction extends ConversionActionBase {
	private static final Logger LOGGER = Logger.getInstance(AvroSchemaToIdlAction.class);

	public AvroSchemaToIdlAction() {
		super("Convert to Avro IDL", AvroSchemaFileType.INSTANCE, AvroIdlFileType.INSTANCE);
	}

	@Override
	protected void convertFile(@NotNull Project project, @NotNull VirtualFile file) {
		final String destinationName = VfsUtil.getNextAvailableName(file.getParent(), file.getNameWithoutExtension(),
			AvroIdlFileType.INSTANCE.getDefaultExtension());

		final Schema schema;
		try {
			schema = new Schema.Parser().parse(file.toNioPath().toFile());
		} catch (IOException e) {
			LOGGER.warn("Failed to parse Avro IDL in " + file.getPresentableName(), e);
			error(project, "Failed to parse Avro IDL in %s: please resolve errors first.\n" +
				"(the error is also written to the idea log)", file.getPresentableName());
			return;
		}
		try {
			final VirtualFile destinationFile = file.getParent().createChildData(this, destinationName);
			try (final OutputStream outputStream = destinationFile.getOutputStream(this);
				 OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
				IdlUtils.writeIdlProtocol(writer, schema.getNamespace(), "`protocol`", schema);
				writer.flush();
			}
			info(project, "Converted AvroProtocol in %s to Avro IDL in %s", file.getPresentableName(), destinationFile.getPresentableName());
		} catch (IOException e) {
			LOGGER.warn("Failed to write Avro IDL to " + destinationName, e);
			error(project, "Failed to write Avro IDL to %s. See the idea log for more details.", destinationName);
		}
	}
}

