package dev.nano.payment;

import dev.nano.clients.payment.PaymentRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import dev.nano.swagger.BaseController;

import java.util.List;

import static dev.nano.payment.PaymentConstant.PAYMENT_URI_REST_API;

@RestController
@RequestMapping(path = PAYMENT_URI_REST_API)
@Tag(name = BaseController.PAYMENT_TAG, description = BaseController.PAYMENT_DESCRIPTION)
@AllArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(
            summary = "Get payment by ID",
            description = "Retrieve detailed information about a specific payment using its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{paymentId}")
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable("paymentId") Long paymentId) {
        log.info("Retrieving payment with id {}", paymentId);
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }

    @Operation(
            summary = "Get all payments",
            description = "Retrieve a list of all payments"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payments retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PaymentDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/list")
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        log.info("Retrieving all payments");
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @Operation(
            summary = "Process new payment",
            description = "Process a new payment transaction for an order"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Payment processed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid payment data"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentRequest payment) {
        log.info("Processing new payment: {}", payment);
        return new ResponseEntity<>(
                paymentService.createPayment(payment),
                HttpStatus.CREATED
        );
    }
}
