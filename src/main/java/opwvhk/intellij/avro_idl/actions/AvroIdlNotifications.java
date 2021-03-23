package opwvhk.intellij.avro_idl.actions;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

import static java.util.Objects.requireNonNull;

public final class AvroIdlNotifications {

	public static void error(Project project, String message, Object... args) {
		showNotification(project, NotificationType.ERROR, message, args);
	}

	public static void warning(Project project, String message, Object... args) {
		showNotification(project, NotificationType.WARNING, message, args);
	}

	public static void info(Project project, String message, Object... args) {
		showNotification(project, NotificationType.INFORMATION, message, args);
	}

	private static void showNotification(Project project, NotificationType type, String message, Object... args) {
		final String formattedMessage = args.length == 0 ? message : String.format(message, args);
		final NotificationGroup notificationGroup = requireNonNull(NotificationGroup.findRegisteredGroup("Avro IDL"));
		notificationGroup.createNotification(formattedMessage, type).notify(project);
	}

	private AvroIdlNotifications() {
		// Utility class.
	}
}
