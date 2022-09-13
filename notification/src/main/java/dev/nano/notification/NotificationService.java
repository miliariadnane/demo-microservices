package dev.nano.notification;

import dev.nano.clients.notification.NotificationRequest;

import java.util.List;

public interface NotificationService {

    public NotificationDTO getNotification(Long notificationId);
    public List<NotificationDTO> getAllNotification();
    public void sendNotification(NotificationRequest notificationRequest);
}
