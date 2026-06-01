package opwvhk.intellij.avro_idl;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public class AvroIdlPluginUtils {
	private static final PluginId MY_PLUGIN_ID = PluginId.getId("net.sf.opk.avro-schema-support");

	@NotNull
	public static IdeaPluginDescriptor getMyPluginDescriptor() {
		return requireNonNull(PluginManagerCore.getPlugin(MY_PLUGIN_ID), "Own description is null: broken plugin!");
	}
}
