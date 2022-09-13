package dev.nano.clients.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment", url = "${clients.payment.url}")
public interface PaymentClient {

    @PostMapping(path = "/api/v1/payments/make-new-payment")
    PaymentResponse createPayment(@RequestBody PaymentRequest payment);
}
