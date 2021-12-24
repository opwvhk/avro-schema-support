package opwvhk.intellij.avro_idl;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.actions.ShowSettingsUtilImpl;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.ui.Messages;
import opwvhk.intellij.avro_idl.actions.AvroIdlNotifications;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;
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
	public static final PluginId OLD_PLUGIN_ID = PluginId.getId("claims.bold.intellij.avro");

	@Override
	public void runActivity(@NotNull Project project) {
		AvroIdlSettings settings = AvroIdlSettings.getInstance();
		IdeaPluginDescriptor plugin = requireNonNull(PluginManagerCore.getPlugin(PluginId.getId("net.sf.opk.avro-schema-support")), "Own description is null: broken plugin!");

		checkForReplacedPlugin(project, plugin.getName());

		String version = plugin.getVersion();
		String oldVersion = settings.getPluginVersion();
		if (oldVersion == null) {
			notifyUserOfNewInstall(project, plugin);
			settings.setPluginVersion(version);
		} else if (!version.equals(oldVersion)) {
			notifyUserOfChanges(project, plugin, oldVersion);
			settings.setPluginVersion(version);
		}
	}

	private void checkForReplacedPlugin(@NotNull Project project, String myName) {
		IdeaPluginDescriptor descriptor = PluginManagerCore.getPlugin(OLD_PLUGIN_ID);
		if (descriptor != null && !PluginManagerCore.isDisabled(OLD_PLUGIN_ID)) {

			// The old Avro plugin by Abigail Buccaneer is both installed and enabled. This can cause problems, so offer to disable it.

			String offendingPluginName = descriptor.getName();
			// Reuses the strings used by the PluginReplacement extension point, but now the other way around.
			String title = IdeBundle.message("plugin.manager.obsolete.plugins.detected.title");
			String message = IdeBundle.message("plugin.manager.replace.plugin.0.by.plugin.1", offendingPluginName, myName);

			AvroIdlNotifications.showNotification(project, NotificationType.WARNING, true, title, message,
				notification -> notification.addAction(NotificationAction.createSimple(IdeBundle.message("button.disable"), () -> {
					PluginManagerCore.disablePlugin(OLD_PLUGIN_ID);
					notification.expire();
				})).addAction(NotificationAction.createSimple(Messages.getNoButton(), notification::expire)));
		}
	}


	private void notifyUserOfNewInstall(@NotNull Project project, @NotNull IdeaPluginDescriptor plugin) {
		//noinspection SpellCheckingInspection
		AvroIdlNotifications.showNotification(project, NotificationType.INFORMATION, true, "Avro IDL Support installed.",
			plugin.getName() + " version " + plugin.getVersion() + " was successfully installed.<br/>" + "If you like, you can customize " +
				"<a href=\"reference.settingsdialog.IDE.editor.colors.Avro IDL#\">Colors</a>, " +
				"<a href=\"preferences.sourceCode.Avro IDL#\">Code Style</a>, and <a href=\"Errors#Avro IDL\">Inspections</a>.",
			notification -> notification.setListener(createUrlOpeningListener(project)));
	}

	private void notifyUserOfChanges(@NotNull Project project, @NotNull IdeaPluginDescriptor plugin, @NotNull String oldVersion) {
		// Collect the changes since the previously installed version.
		StringBuilder changes = new StringBuilder();
		Matcher matcher = Pattern.compile("(?s)<ul data-version=\"(?<version>[^\"]+)\">.*?</ul>").matcher(plugin.getChangeNotes());
		int count = 0;
		while (matcher.find()) {
			count++;
			if (count > 3) {
				break;
			}
			final String version = matcher.group("version");
			if (version.equals(oldVersion)) {
				break;
			}
			changes.append(version).append(":").append(matcher.group());
		}
		AvroIdlNotifications.showNotification(project, NotificationType.INFORMATION, false,
			"Avro IDL Support updated to v" + plugin.getVersion(),
			"All <a href=\"#Avro IDL\">settings are here</a>.<br/><b>This is what has changed:</b><br/><br/>" + changes,
			notification -> notification.setListener(createUrlOpeningListener(project)));
	}

	/**
	 * Create a notification listener to support links. This extends the default (supporting hyperlinks), to allow linking into the settings dialog.
	 * <p>
	 * To link into the settings, build a link with: [a settings id] + '#' + [a search filter]
	 * <p>
	 * Useful settings ids are in searchableOptions.xml (created by building the plugin), using the XPath: //configurable[configurable_name="Avro IDL"]@id
	 *
	 * @param project the current project
	 * @return the notification listener
	 */
	private @NotNull NotificationListener.UrlOpeningListener createUrlOpeningListener(@NotNull Project project) {
		return new NotificationListener.UrlOpeningListener(false) {
			@Override
			protected void hyperlinkActivated(@NotNull Notification notification, @NotNull HyperlinkEvent event) {
				final String link = event.getDescription();
				if (link != null && link.contains("#")) {
					int hashPos = link.indexOf("#");
					String idToSelect = link.substring(0, hashPos); // link.contains("#"), so hashPos >= 0
					String searchFilter = link.substring(hashPos + 1);
					if (!project.isDisposed()) {
						// Values for idToSelect are in searchableOptions.xml; use this XPath: /option/configurable[configurable_name="Avro IDL"]@id
						// (note: searchableOptions.xml is created when building the plugin)
						ShowSettingsUtilImpl.showSettingsDialog(project, idToSelect, searchFilter);
					}
				} else {
					super.hyperlinkActivated(notification, event);
				}
			}
		};
	}
}
