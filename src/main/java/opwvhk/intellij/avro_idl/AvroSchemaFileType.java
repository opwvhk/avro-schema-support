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

	@NotNull
	@Override
	public String getName() {
		return "Avro Schema";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Apache Avro™ Schema";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "avsc";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return AvroIdlIcons.FILE;
	}
}
