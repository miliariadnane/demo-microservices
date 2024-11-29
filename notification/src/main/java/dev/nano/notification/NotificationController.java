package dev.nano.notification;

import dev.nano.clients.notification.NotificationRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.nano.notification.NotificationConstant.NOTIFICATION_URI_REST_API;

@RestController
@RequestMapping(path = NOTIFICATION_URI_REST_API)
@AllArgsConstructor @Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationDTO> getNotification(@PathVariable("notificationId") Long notificationId) {
        log.info("Retrieving notification with id {}", notificationId);
        return ResponseEntity.ok(notificationService.getNotification(notificationId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationDTO>> getAllNotification() {
        log.info("Retrieving all notifications");
        return ResponseEntity.ok(notificationService.getAllNotification());
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody NotificationRequest notificationRequest) {
        log.info("Sending new notification {}", notificationRequest);
        notificationService.sendNotification(notificationRequest);
        return ResponseEntity.ok().build();
    }
}
