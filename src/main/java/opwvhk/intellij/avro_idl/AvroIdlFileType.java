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

    @NotNull
    @Override
    public String getName() {
        return "Avro IDL";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Apache Avro™ IDL";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "avdl";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AvroIdlIcons.FILE;
    }
}
