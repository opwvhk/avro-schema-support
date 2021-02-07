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
	@NotNull
	public static final AvroProtocolLanguage INSTANCE = new AvroProtocolLanguage();

	protected AvroProtocolLanguage() {
		super("AvroProtocol", "text/vnd.apache.avro-protocol");
	}
}
