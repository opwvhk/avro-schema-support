package opwvhk.intellij.avro_idl.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import opwvhk.intellij.avro_idl.AvroIdlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.intellij.openapi.application.ModalityState.nonModal;
import static java.util.Objects.requireNonNull;

public final class AvroIdlNotifications {

	public static void showNotification(@NotNull Project project, @NotNull NotificationType type,
	                                    @NotNull String title, @Nullable String message,
	                                    @Nullable Consumer<Notification> configurer) {
		//final NotificationGroup notificationGroup = requireNonNull(
		//		NotificationGroup.findRegisteredGroup("avro.idl.updates"));
		final NotificationGroup notificationGroup = requireNonNull(NotificationGroupManager.getInstance()
				.getNotificationGroup("avro.idl.updates"));

		Notification notification = notificationGroup.createNotification(title, message == null ? "" : message, type);
		notification.setIcon(AvroIdlIcons.PLUGIN_ICON);
		if (configurer != null) {
			configurer.accept(notification);
		}

		// During hot-install, startup activities run concurrently with the plugin registration.
		// This can break notifications, so invoke the notification later.
		// Also trigger it as a read action, so it'll be triggered when plugin registration is complete.
		Application application = ApplicationManager.getApplication();
		application.invokeLater(() -> application.runReadAction(() -> notification.notify(project)), nonModal());
	}

	private AvroIdlNotifications() {
		// Utility class.
	}
}
