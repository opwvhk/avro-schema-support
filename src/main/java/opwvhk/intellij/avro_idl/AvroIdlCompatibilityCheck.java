package opwvhk.intellij.avro_idl;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.ui.Messages;
import opwvhk.intellij.avro_idl.actions.AvroIdlNotifications;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;
import static opwvhk.intellij.avro_idl.AvroIdlPluginUpdateStartupActivity.MY_PLUGIN_ID;

/**
 * Startup activity (actually a preloading activity because it runs earlier) to check if a known incompatible plugin is active. If so, offer to disable it.
 *
 * <p>Reason to use this and not {@link com.intellij.ide.plugins.PluginReplacement PluginReplacement} is because the latter is backwards: you cannot define a
 * replacement for a conflicting but unmaintained plugin, as it needs to be defined in the plugin to be replaced (which you cannot do, because both the source
 * and the installation account are inaccessible).</p>
 */
public class AvroIdlCompatibilityCheck implements StartupActivity.DumbAware {
	public static final PluginId OLD_PLUGIN_ID = PluginId.getId("claims.bold.intellij.avro");

	@Override
	public void runActivity(@NotNull Project project) {
		final IdeaPluginDescriptor myDescriptor = requireNonNull(PluginManagerCore.getPlugin(MY_PLUGIN_ID), "Own description is null: broken plugin!");
		final String myName = myDescriptor.getName();
		final IdeaPluginDescriptor descriptor = PluginManagerCore.getPlugin(OLD_PLUGIN_ID);
		if (descriptor != null && !PluginManagerCore.isDisabled(OLD_PLUGIN_ID)) {

			// The old Avro plugin by Abigail Buccaneer is both installed and enabled. This can cause problems, so offer to disable it.

			final String offendingPluginName = descriptor.getName();
			// Reuses the strings used by the PluginReplacement extension point, but now the other way around.
			final String title = IdeBundle.message("plugin.manager.obsolete.plugins.detected.title");
			final String message = IdeBundle.message("plugin.manager.replace.plugin.0.by.plugin.1", offendingPluginName, myName);

			AvroIdlNotifications.showNotification(project, NotificationType.WARNING, true, title, message,
				notification -> notification
					.addAction(NotificationAction.createSimple(IdeBundle.message("button.disable"), () -> {
						PluginManagerCore.disablePlugin(OLD_PLUGIN_ID);
						notification.expire();
					}))
					.addAction(NotificationAction.createSimple(Messages.getNoButton(), notification::expire))
			);
		}
	}
}
