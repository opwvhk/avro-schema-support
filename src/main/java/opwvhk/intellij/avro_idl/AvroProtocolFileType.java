package opwvhk.intellij.avro_idl;

import com.intellij.json.json5.Json5Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AvroProtocolFileType extends LanguageFileType {
	/**
	 * A shared instance of AvroProtocolFileType.
	 */
	public static final AvroProtocolFileType INSTANCE = new AvroProtocolFileType();

	protected AvroProtocolFileType() {
		super(AvroProtocolLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "Avro Protocol";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Apache Avro™ Protocol";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "avpr";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return AvroIdlIcons.FILE;
	}
}
