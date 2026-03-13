package dev.nano.clients.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "payment", url = "${clients.payment.url}")
public interface PaymentClient {

    @GetMapping(path = "/api/v1/payments/{paymentId}")
    PaymentResponse getPayment(@PathVariable("paymentId") Long paymentId);

    @GetMapping(path = "/api/v1/payments/list")
    List<PaymentResponse> getAllPayments();

    @PostMapping(path = "/api/v1/payments")
    PaymentResponse createPayment(@RequestBody PaymentRequest payment);
}
