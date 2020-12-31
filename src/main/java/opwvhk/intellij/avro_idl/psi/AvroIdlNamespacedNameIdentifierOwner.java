package opwvhk.intellij.avro_idl.psi;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

public interface AvroIdlNamespacedNameIdentifierOwner extends AvroIdlAnnotatedNameIdentifierOwner {
	/**
	 * Returns the fully qualified name of the element.
	 *
	 * @return the full element name
	 */
	@Nullable
	@NonNls
	String getFullName();
}
