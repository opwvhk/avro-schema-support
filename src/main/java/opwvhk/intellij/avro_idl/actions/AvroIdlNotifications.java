package opwvhk.intellij.avro_idl.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.impl.NotificationFullContent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public final class AvroIdlNotifications {

	public static void showNotification(@NotNull Project project, @NotNull NotificationType type, boolean important,
	                                    @Nullable String title, @NotNull String message, @Nullable Consumer<Notification> configurer) {
		final NotificationGroup notificationGroup = requireNonNull(NotificationGroup.findRegisteredGroup(important ? "Avro IDL Important" : "Avro IDL"));
		Notification notification = createNotification(notificationGroup, important, title == null ? "" : title, message, type);
		if (configurer != null) {
			configurer.accept(notification);
		}
		notification.notify(project);
	}

	private static @NotNull Notification createNotification(@NotNull NotificationGroup notificationGroup, boolean fullContent, @NotNull String title,
	                                                        @NotNull String content, @NotNull NotificationType type) {
		final Notification notification;
		if (fullContent) {
			notification = new FullContentNotification(notificationGroup, title, content, type);
		} else {
			notification = notificationGroup.createNotification(title, content, type);
		}
		notification.setIcon(notificationGroup.getIcon());
		return notification;
	}

	private AvroIdlNotifications() {
		// Utility class.
	}

	private static class FullContentNotification extends Notification implements NotificationFullContent {
		public FullContentNotification(@NotNull NotificationGroup notificationGroup, @NotNull String title, @NotNull String content,
		                               @NotNull NotificationType type) {
			super(notificationGroup.getDisplayId(), title, content, type);
		}
	}
}
