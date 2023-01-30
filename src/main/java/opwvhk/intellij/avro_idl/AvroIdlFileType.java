package opwvhk.intellij.avro_idl;

import javax.swing.*;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Represents the file type for Apache Avro™ IDL files.
 */
public class AvroIdlFileType extends LanguageFileType {
    /**
     * A shared instance of AvroIdlFileType.
     */
    public static final AvroIdlFileType INSTANCE;

	static {
		try {
			INSTANCE = new AvroIdlFileType();
		} catch (Throwable t) {
			Logger.getInstance(AvroIdlFileType.class).error("Failed to initialise AvroIdlFileType", t);
			throw t;
		}
	}

	protected AvroIdlFileType() {
        super(AvroIdlLanguage.INSTANCE);
    }

    @Override
    @NotNull
    public String getName() {
        return "Avro IDL";
    }

    @Override
    @NotNull
    public String getDescription() {
        return "Apache Avro™ IDL";
    }

    @Override
    @NotNull
    public String getDefaultExtension() {
        return "avdl";
    }

    @Override
    @Nullable
    public Icon getIcon() {
        return AvroIdlIcons.FILE;
    }
}
