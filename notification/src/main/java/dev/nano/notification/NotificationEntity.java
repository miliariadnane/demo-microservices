package dev.nano.notification;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString @SuperBuilder
@Entity(name = "Notification")
@Table(name = "notification")
public class NotificationEntity {

    @Id
    @SequenceGenerator(
            name = "notification_sequence",
            sequenceName = "notification_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "customer_id",
            nullable = false
    )
    private Long customerId;

    @Column(
            name = "customer_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String customerName;

    @Column(
            name = "customer_email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String customerEmail;

    @Column(
            name = "sender",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String sender;

    @Column(
            name = "message",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String message;

    @Column(
            name = "sent_at",
            nullable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime sentAt;
}
