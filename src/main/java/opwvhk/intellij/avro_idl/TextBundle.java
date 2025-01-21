package opwvhk.intellij.avro_idl;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class TextBundle {
	// Note: SHOULD match <resource-bundle> defined in plugin.xml
	private static final @NonNls String BUNDLE = "messages.TextBundle";
	private static final DynamicBundle INSTANCE = new DynamicBundle(TextBundle.class, BUNDLE);
	// Also access diagnostic messages in IntelliJ (the class originally used to access them is now marked internal)
	private static final @NonNls String DIAGNOSTIC_BUNDLE = "messages.DiagnosticBundle";
	private static final DynamicBundle DIAGNOSTIC_INSTANCE = new DynamicBundle(TextBundle.class, DIAGNOSTIC_BUNDLE);

	public static @NotNull @Nls String message(
			@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
			Object... params) {
		return INSTANCE.getMessage(key, params);
	}

	public static @NotNull @Nls String diagnosticMessage(
			@NotNull @PropertyKey(resourceBundle = DIAGNOSTIC_BUNDLE) String key,
			Object... params) {
		return DIAGNOSTIC_INSTANCE.getMessage(key, params);
	}
}
