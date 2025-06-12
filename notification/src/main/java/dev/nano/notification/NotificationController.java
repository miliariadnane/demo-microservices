package dev.nano.notification;

import dev.nano.clients.notification.NotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import dev.nano.swagger.BaseController;

import java.util.List;

import static dev.nano.notification.NotificationConstant.NOTIFICATION_URI_REST_API;

@RestController
@RequestMapping(path = NOTIFICATION_URI_REST_API)
@Tag(name = BaseController.NOTIFICATION_TAG, description = BaseController.NOTIFICATION_DESCRIPTION)
@AllArgsConstructor @Slf4j
@PreAuthorize("hasRole('app_admin')")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
            summary = "Get notification by ID",
            description = "Retrieve detailed information about a specific notification using its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notification found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Notification not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationDTO> getNotification(@PathVariable("notificationId") Long notificationId) {
        log.info("Retrieving notification with id {}", notificationId);
        return ResponseEntity.ok(notificationService.getNotification(notificationId));
    }

    @Operation(
            summary = "Get all notifications",
            description = "Retrieve a list of all notifications with their details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notifications retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NotificationDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<NotificationDTO>> getAllNotification() {
        log.info("Retrieving all notifications");
        return ResponseEntity.ok(notificationService.getAllNotification());
    }

    @Operation(
            summary = "Send new notification",
            description = "Send a notification to a customer through configured channels (email, SMS, etc.)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notification sent successfully"
            ),
            @ApiResponse(responseCode = "400", description = "Invalid notification data"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error or notification delivery failure")
    })
    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody NotificationRequest notificationRequest) {
        log.info("Sending new notification {}", notificationRequest);
        notificationService.sendNotification(notificationRequest);
        return ResponseEntity.ok().build();
    }
}
