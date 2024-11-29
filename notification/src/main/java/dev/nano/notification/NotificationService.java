package dev.nano.notification;

import dev.nano.clients.notification.NotificationRequest;

import java.util.List;

public interface NotificationService {
    NotificationDTO getNotification(Long notificationId);
    List<NotificationDTO> getAllNotification();
    void sendNotification(NotificationRequest notificationRequest);
}
