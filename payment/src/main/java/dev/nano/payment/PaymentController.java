package dev.nano.payment;

import dev.nano.clients.payment.PaymentRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static dev.nano.payment.PaymentConstant.PAYMENT_URI_REST_API;

@RestController
@RequestMapping(path = PAYMENT_URI_REST_API)
@AllArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping(
            path = "/{paymentId}",
            produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable("paymentId") Long paymentId) {
        log.info("Retrieving payment with id {}", paymentId);
        return new ResponseEntity<>(
                paymentService.getPayment(paymentId),
                HttpStatus.OK
        );
    }

    @GetMapping(
            produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        log.info("Retrieving all payments");
        return new ResponseEntity<>(
                paymentService.getAllPayments(),
                HttpStatus.OK
        );
    }

    @PostMapping(path = "/make-new-payment")
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentRequest payment) {

        return new ResponseEntity<>(
                paymentService.createPayment(payment),
                HttpStatus.CREATED
        );
    }
}
