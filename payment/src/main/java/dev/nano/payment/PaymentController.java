package dev.nano.payment;

import dev.nano.clients.payment.PaymentRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.nano.payment.PaymentConstant.PAYMENT_URI_REST_API;

@RestController
@RequestMapping(path = PAYMENT_URI_REST_API)
@AllArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable("paymentId") Long paymentId) {
        log.info("Retrieving payment with id {}", paymentId);
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        log.info("Retrieving all payments");
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PostMapping("/make-new-payment")
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentRequest payment) {
        log.info("Processing new payment: {}", payment);
        return new ResponseEntity<>(
                paymentService.createPayment(payment),
                HttpStatus.CREATED
        );
    }
}
