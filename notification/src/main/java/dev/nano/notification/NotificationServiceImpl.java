package dev.nano.notification;

import dev.nano.clients.notification.NotificationRequest;
import dev.nano.notification.email.EmailService;
import dev.nano.exceptionhandler.business.NotificationException;
import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static dev.nano.notification.NotificationConstant.*;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final EmailService emailService;

    @Override
    public NotificationDTO getNotification(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .map(notificationMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(NOTIFICATION_NOT_FOUND, notificationId)
                ));
    }

    @Override
    public List<NotificationDTO> getAllNotification() {
        List<NotificationEntity> notifications = notificationRepository.findAll();
        if (notifications.isEmpty()) {
            throw new ResourceNotFoundException(NO_NOTIFICATIONS_FOUND);
        }
        return notificationMapper.toListDTO(notifications);
    }

    @Override
    public void sendNotification(NotificationRequest notificationRequest) {
        try {
            NotificationEntity notification = NotificationEntity.builder()
                    .customerId(notificationRequest.getCustomerId())
                    .customerName(notificationRequest.getCustomerName())
                    .customerEmail(notificationRequest.getCustomerEmail())
                    .sender("nanodev")
                    .message(notificationRequest.getMessage())
                    .sentAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);

            emailService.send(
                    notificationRequest.getCustomerEmail(),
                    buildEmail(notificationRequest.getCustomerName(), notificationRequest.getMessage()),
                    notificationRequest.getSender()
            );
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
            throw new NotificationException(String.format(NOTIFICATION_SEND_ERROR, e.getMessage()));
        }
    }

    private String buildEmail(String name, String message) {
        return String.format("""
                Mail sent to: %s
                <p>%s</p>
                """, name, message);
    }
}
