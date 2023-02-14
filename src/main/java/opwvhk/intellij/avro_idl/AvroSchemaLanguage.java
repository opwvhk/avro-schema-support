package opwvhk.intellij.avro_idl;

import com.intellij.json.JsonLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the Apache Avro™ Schema language.
 */
public class AvroSchemaLanguage extends JsonLanguage {
	/**
	 * A shared instance of AvroIdlLanguage.
	 */
	@NotNull
	public static final AvroSchemaLanguage INSTANCE = new AvroSchemaLanguage();

	protected AvroSchemaLanguage() {
		super("Avro Schema", "text/vnd.apache.avro-schema");
	}
}
