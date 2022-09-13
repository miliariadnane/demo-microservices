package dev.nano.notification;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String sender;
    private String message;
    private LocalDateTime sentAt;
}
