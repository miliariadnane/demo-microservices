package dev.nano.notification;

import dev.nano.clients.notification.NotificationRequest;
import dev.nano.notification.aws.AWSEmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final AWSEmailService emailService;

    @Override
    public NotificationDTO getNotification(Long notificationId) {

        NotificationEntity product = notificationRepository.findById(notificationId).orElseThrow(() ->
                new IllegalStateException("Notification not found"));

        return notificationMapper.toDTO(product);
    }

    @Override
    public List<NotificationDTO> getAllNotification() {
        List<NotificationEntity> listProduct = notificationRepository.findAll();
        return notificationMapper.toListDTO(listProduct);
    }

    @Override
    public void sendNotification(NotificationRequest notificationRequest) {

        notificationRepository.save(NotificationEntity.builder()
                .customerId(notificationRequest.getCustomerId())
                .customerName(notificationRequest.getCustomerName())
                .customerEmail(notificationRequest.getCustomerEmail())
                .sender("nanodev")
                .message(notificationRequest.getMessage())
                .sentAt(LocalDateTime.now())
                .build());

        emailService.send(
                notificationRequest.getCustomerEmail(),
                buildEmail(notificationRequest.getCustomerName(), notificationRequest.getMessage()),
                notificationRequest.getSender()
        );
    }

    public String buildEmail(String name, String message) {
        return """
                Mail sent to: ${name}
                <p>${message}</p>
        """;

    }
}
