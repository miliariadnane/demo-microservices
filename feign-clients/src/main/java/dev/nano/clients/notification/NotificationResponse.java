package dev.nano.clients.notification;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long customerId,
        String customerName,
        String customerEmail,
        String sender,
        String message,
        LocalDateTime sentAt
) {}
