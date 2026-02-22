package dev.nano.clients.payment;

public record PaymentRequest(
        Long customerId,
        String customerName,
        String customerEmail,
        Long orderId
) {}
