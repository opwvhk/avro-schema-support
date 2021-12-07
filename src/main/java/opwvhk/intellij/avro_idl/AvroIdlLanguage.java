package opwvhk.intellij.avro_idl;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the Apache Avro™ IDL language.
 */
public class AvroIdlLanguage extends Language {
    /**
     * A shared instance of AvroIdlLanguage.
     */
    public static final @NotNull AvroIdlLanguage INSTANCE = new AvroIdlLanguage();

    protected AvroIdlLanguage() {
        super("Avro IDL", "text/vnd.apache.avro-avro_idl");
    }
}
