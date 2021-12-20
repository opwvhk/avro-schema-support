package opwvhk.intellij.avro_idl;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import opwvhk.intellij.avro_idl.actions.AvroIdlNotifications;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Shows update notification.
 */
public class AvroIdlPluginUpdateStartupActivity implements StartupActivity.DumbAware {
	public static final PluginId MY_PLUGIN_ID = PluginId.getId("net.sf.opk.avro-schema-support");

	@Override
	public void runActivity(@NotNull Project project) {
		AvroIdlSettings settings = AvroIdlSettings.getInstance();
		IdeaPluginDescriptor plugin = requireNonNull(PluginManagerCore.getPlugin(MY_PLUGIN_ID), "Own description is null: broken plugin!");
		String version = plugin.getVersion();
		String oldVersion = settings.getPluginVersion();
		if (oldVersion == null) {
			settings.setPluginVersion(version);
		} else if (!version.equals(oldVersion)) {
			notifyUserOfChanges(project, plugin, oldVersion);

			settings.setPluginVersion(version);
		}
	}

	private void notifyUserOfChanges(@NotNull Project project, IdeaPluginDescriptor plugin, String oldVersion) {
		// Collect the changes since the previously installed version.
		StringBuilder changes = new StringBuilder();
		Matcher matcher = Pattern.compile("(?s)<ul data-version=\"(?<version>[^\"]+)\">.*?</ul>").matcher(plugin.getChangeNotes());
		int count = 0;
		while (matcher.find()) {
			final String version = matcher.group("version");
			if (version.equals(oldVersion)) {
				break;
			}
			count++;
			if (count > 5) {
				break;
			}
			changes.append(version).append(":").append(matcher.group());
		}
		// During hot-install, startup activities run concurrently with the plugin registration. This can break notifications (and more).
		// Invoke the notification later as read action, so it'll be triggered when plugin registration is complete.
		ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication()
			.runReadAction(() -> AvroIdlNotifications.showNotification(project, NotificationType.INFORMATION, false,
				"Avro IDL Support updated to version " + plugin.getVersion(),
				"This is what has changed:<br/><br/>" + changes,
				notification -> notification.setListener(new NotificationListener.UrlOpeningListener(false)))
			)
		);
	}
}
