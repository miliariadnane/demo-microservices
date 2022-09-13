package dev.nano.clients.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String sender;
    private String message;
    private LocalDateTime sentAt;
}
