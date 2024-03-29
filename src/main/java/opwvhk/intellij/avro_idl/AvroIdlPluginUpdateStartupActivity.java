package opwvhk.intellij.avro_idl;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.actions.ShowSettingsUtilImpl;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.ui.Messages;
import opwvhk.intellij.avro_idl.actions.AvroIdlNotifications;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Startup activity (actually a preloading activity because it runs earlier) to check if a known incompatible plugin is active. If so, offer to disable it.
 *
 * <p>Reason to use this and not {@link com.intellij.ide.plugins.PluginReplacement PluginReplacement} is because the latter is backwards: you cannot define a
 * replacement for a conflicting but unmaintained plugin, as it needs to be defined in the plugin to be replaced (which you cannot do, because both the source
 * and the installation account are inaccessible).</p>
 */
public class AvroIdlPluginUpdateStartupActivity implements StartupActivity.DumbAware {
	private static final PluginId MY_PLUGIN_ID = PluginId.getId("net.sf.opk.avro-schema-support");
	public static final PluginId OLD_PLUGIN_ID = PluginId.getId("claims.bold.intellij.avro");
	private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";
	private static final Pattern CHANGE_NOTES_PATTERN = Pattern.compile(
			"(?s)<ul data-version=\"(?<version>[^\"]+)\">.*?</ul>");

	@NotNull
	public static IdeaPluginDescriptor getMyPluginDescriptor() {
		return requireNonNull(PluginManagerCore.getPlugin(MY_PLUGIN_ID), "Own description is null: broken plugin!");
	}

	@Override
	public void runActivity(@NotNull Project project) {
		AvroIdlSettings settings = AvroIdlSettings.getInstance();
		IdeaPluginDescriptor plugin = getMyPluginDescriptor();

		checkForReplacedPlugin(project, plugin.getName());

		String oldVersion = settings.getPluginVersion();
		String newVersion = versionOf(plugin);

		notifyUserOfUpdate(project, plugin, newVersion, oldVersion);
		settings.setPluginVersion(newVersion);
	}

	@NotNull
	private String versionOf(IdeaPluginDescriptor plugin) {
		String pluginVersion = plugin.getVersion();
		if (pluginVersion.endsWith(SNAPSHOT_SUFFIX)) {
			return pluginVersion.substring(0, pluginVersion.length() - SNAPSHOT_SUFFIX.length());
		} else {
			return pluginVersion;
		}
	}

	private void checkForReplacedPlugin(@NotNull Project project, String myName) {
		IdeaPluginDescriptor descriptor = PluginManagerCore.getPlugin(OLD_PLUGIN_ID);
		if (descriptor != null && !PluginManagerCore.isDisabled(OLD_PLUGIN_ID)) {

			// The old Avro plugin by Abigail Buccaneer is both installed and enabled.
			// This can cause problems, so offer to disable it.

			String offendingPluginName = descriptor.getName();
			// Reuses the strings used by the PluginReplacement extension point, but now the other way around.
			String title = IdeBundle.message("plugin.manager.obsolete.plugins.detected.title");
			String message = IdeBundle.message("plugin.manager.replace.plugin.0.by.plugin.1", offendingPluginName,
					myName);

			AvroIdlNotifications.showNotification(project, NotificationType.WARNING, title, message,
					notification -> notification
							.addAction(NotificationAction.createSimpleExpiring(IdeBundle.message("button.disable"),
									() -> PluginManager.disablePlugin(OLD_PLUGIN_ID.getIdString())))
							.addAction(NotificationAction.createSimpleExpiring(Messages.getNoButton(), () -> {})));
		}
	}


	private void notifyUserOfUpdate(@NotNull Project project, @NotNull IdeaPluginDescriptor plugin,
	                                @NotNull String newVersion, @Nullable String oldVersion) {
		String changeNotes = plugin.getChangeNotes();
		String notificationTitle = oldVersion == null ?
				plugin.getName() + " " + newVersion + " installed." :
				plugin.getName() + " updated to version " + newVersion;
		Consumer<Notification> addNotificationActions = notification -> {
			// Values for idToSelect are in searchableOptions.xml; use this XPath: /option/configurable[configurable_name="AvroIDL"]@id
			// (note: searchableOptions.xml is created when building the plugin)
			notification.addAction(NotificationAction.createSimple("Open preferences",
					() -> ShowSettingsUtilImpl.showSettingsDialog(project, "", "Avro IDL")));
			notification.addAction(NotificationAction.createSimple("Ask questions",
					() -> BrowserLauncher.getInstance().browse(URI.create(
							"https://github.com/opwvhk/avro-schema-support/discussions"))));
			notification.addAction(NotificationAction.createSimple("Report issues",
					() -> BrowserLauncher.getInstance()
							.browse(URI.create("https://github.com/opwvhk/avro-schema-support/issues"))));
		};

		if (oldVersion != null) {
			CharSequence changes = collectNewChanges(oldVersion, changeNotes);
			if (changes.length() > 0) {
				AvroIdlNotifications.showNotification(project, NotificationType.INFORMATION,
						notificationTitle, "This is what has changed:</b><br/><br/>" + changes,
						addNotificationActions);
			}
		} else {
			AvroIdlNotifications.showNotification(project, NotificationType.INFORMATION, notificationTitle, null,
					addNotificationActions);
		}
	}

	@NotNull
	private static CharSequence collectNewChanges(@NotNull String oldVersion, String changeNotes) {
		if (changeNotes == null) {
			return "";
		}

		// Collect the changes since the previously installed version (but for at most 3 versions).
		StringBuilder changes = new StringBuilder();
		Matcher matcher = CHANGE_NOTES_PATTERN.matcher(changeNotes);
		matcher.results()
				.takeWhile(mr -> !mr.group(1).equals(oldVersion))
				.limit(3)
				.forEach(mr -> {
					final String version = mr.group(1);
					changes.append(version).append(":").append(matcher.group());
				});
		return changes;
	}
}
