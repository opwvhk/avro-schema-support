package opwvhk.intellij.avro_idl;

import com.intellij.json.JsonLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AvroSchemaFileType extends LanguageFileType {
	/**
	 * A shared instance of AvroSchemaFileType.
	 */
	public static final AvroSchemaFileType INSTANCE = new AvroSchemaFileType();

	protected AvroSchemaFileType() {
		super(JsonLanguage.INSTANCE);
	}

	@Override
	@NotNull
	public String getName() {
		return "Avro Schema";
	}

	@Override
	public @Nls @NotNull String getDisplayName() {
		return getName();
	}

	@Override
	@NotNull
	public String getDescription() {
		// False positive: this "description" is used as a name
		//noinspection DialogTitleCapitalization
		return TextBundle.message("filetype.avsc.description");
	}

	@Override
	@NotNull
	public String getDefaultExtension() {
		return "avsc";
	}

	@Override
	@Nullable
	public Icon getIcon() {
		return AvroIdlIcons.AVSC_FILE;
	}
}
