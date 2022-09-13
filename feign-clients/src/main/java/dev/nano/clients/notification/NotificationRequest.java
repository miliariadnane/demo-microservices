package dev.nano.clients.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class NotificationRequest {
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String sender;
    private String message;
}
