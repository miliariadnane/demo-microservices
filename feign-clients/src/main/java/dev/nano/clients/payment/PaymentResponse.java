package dev.nano.clients.payment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private Long customerId;
    private Long orderId;
    private LocalDateTime createAt;
}
