package opwvhk.intellij.avro_idl;

import com.intellij.json.json5.Json5Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AvroSchemaFileType extends LanguageFileType {
	/**
	 * A shared instance of AvroSchemaFileType.
	 */
	public static final AvroSchemaFileType INSTANCE = new AvroSchemaFileType();

	protected AvroSchemaFileType() {
		super(AvroSchemaLanguage.INSTANCE);
	}

	@Override
	public @NotNull String getName() {
		return "Avro Schema";
	}

	@Override
	public @NotNull String getDescription() {
		return "Apache Avro™ Schema";
	}

	@Override
	public @NotNull String getDefaultExtension() {
		return "avsc";
	}

	@Override
	public @Nullable Icon getIcon() {
		return AvroIdlIcons.FILE;
	}
}
