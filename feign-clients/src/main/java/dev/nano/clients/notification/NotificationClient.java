package dev.nano.clients.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification", url = "${clients.notification.url}")
public interface NotificationClient {

    @PostMapping(path = "/api/v1/notifications/send")
    NotificationResponse sendNotification(@RequestBody NotificationRequest notificationRequest);
}
