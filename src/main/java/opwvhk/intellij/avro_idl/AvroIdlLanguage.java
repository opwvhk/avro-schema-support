package opwvhk.intellij.avro_idl;

import com.intellij.lang.Language;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the Apache Avro™ IDL language.
 */
public class AvroIdlLanguage extends Language {
    /**
     * A shared instance of AvroIdlLanguage.
     */
    @NotNull
    public static final AvroIdlLanguage INSTANCE;

	static {
		try {
			INSTANCE = new AvroIdlLanguage();
		} catch (Throwable e) {
			try {
				Logger.getInstance(AvroIdlLanguage.class).error("Failed to instantiate AvroIdlLanguage: ",e);
			} catch (Throwable ex) {
				// As it seems this really is the problem within this plugin, also have a fallback in case logging the error fails...
				System.err.println("Failed to instantiate AvroIdlLanguage: " + e.getMessage());
				e.printStackTrace(System.err);
				System.err.println("And failed to create logger: " + ex.getMessage());
				ex.printStackTrace(System.err);
				e.addSuppressed(ex);
			}
			throw e;
		}
	}

	protected AvroIdlLanguage() {
        super("Avro IDL", "text/vnd.apache.avro-avro_idl");
    }
}
