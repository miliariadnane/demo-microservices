package dev.nano.notification;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificationConstant {
    public static final String NOTIFICATION_URI_REST_API = "/api/v1/notifications";
    public static final String NOTIFICATION_NOT_FOUND = "Notification with ID %d not found";
    public static final String NOTIFICATION_SEND_ERROR = "Failed to send notification: %s";
    public static final String NO_NOTIFICATIONS_FOUND = "No notifications found";
}

