package dev.nano.notification;

import dev.nano.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.nano.notification.NotificationConstant.NOTIFICATION_URI_REST_API;

@RestController
@RequestMapping(path = NOTIFICATION_URI_REST_API)
@AllArgsConstructor @Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(
        path = "/{notificationId}",
        produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<NotificationDTO> getNotification(@PathVariable("notificationId") Long notificationId) {
        log.info("Retrieving notification with id {}", notificationId);
        return new ResponseEntity<>(
                notificationService.getNotification(notificationId),
                HttpStatus.OK
        );
    }

    @GetMapping(
        path = "/all",
        produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<NotificationDTO>> getAllNotification() {
        log.info("Retrieving all notifications");
        return new ResponseEntity<>(
                notificationService.getAllNotification(),
                HttpStatus.OK
        );
    }

    @PostMapping(path = "/send")
    public void sendNotification(@RequestBody NotificationRequest notificationRequest) {
        log.info("Sending new notification {}", notificationRequest);
        notificationService.sendNotification(notificationRequest);
    }
}
