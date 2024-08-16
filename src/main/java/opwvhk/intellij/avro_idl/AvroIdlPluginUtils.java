package opwvhk.intellij.avro_idl;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public class AvroIdlPluginUtils {
	private static final PluginId MY_PLUGIN_ID = PluginId.getId("net.sf.opk.avro-schema-support");
	public static final PluginId OLD_PLUGIN_ID = PluginId.getId("claims.bold.intellij.avro");

	@NotNull
	public static IdeaPluginDescriptor getMyPluginDescriptor() {
		return requireNonNull(PluginManagerCore.getPlugin(MY_PLUGIN_ID), "Own description is null: broken plugin!");
	}

	@Nullable
	public static IdeaPluginDescriptor getConflictingPluginDescriptor() {
		return PluginManagerCore.getPlugin(OLD_PLUGIN_ID);
	}
}
