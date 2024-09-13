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

	public static @NotNull @Nls String message(
			@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
			Object... params) {
		return INSTANCE.getMessage(key, params);
	}
}
