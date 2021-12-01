package opwvhk.intellij.avro_idl;

import com.intellij.json.JsonLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the Apache Avro™ Protocol language.
 */
public class AvroProtocolLanguage extends JsonLanguage {
	/**
	 * A shared instance of AvroIdlLanguage.
	 */
    public static final @NotNull AvroProtocolLanguage INSTANCE = new AvroProtocolLanguage();

	protected AvroProtocolLanguage() {
		super("Avro Protocol", "text/vnd.apache.avro-protocol");
	}
}
