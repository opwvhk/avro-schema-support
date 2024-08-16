package opwvhk.intellij.avro_idl;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Represents the file type for Apache Avro™ IDL files.
 */
public class AvroIdlFileType extends LanguageFileType {
	/**
	 * A shared instance of AvroIdlFileType.
	 */
	public static final AvroIdlFileType INSTANCE = new AvroIdlFileType();

	protected AvroIdlFileType() {
		super(AvroIdlLanguage.INSTANCE);
	}

	@Override
	@NotNull
	public String getName() {
		return "AvroIDL";
	}

	@Override
	@NotNull
	public String getDescription() {
		return TextBundle.message("filetype.avdl.description");
	}

	@Override
	@NotNull
	public String getDefaultExtension() {
		return "avdl";
	}

	@Override
	@Nullable
	public Icon getIcon() {
		return AvroIdlIcons.AVDL_FILE;
	}
}
