package dev.nano.clients.payment;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long orderId;
}
