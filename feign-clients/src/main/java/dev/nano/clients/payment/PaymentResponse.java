package dev.nano.clients.payment;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long customerId,
        Long orderId,
        LocalDateTime createAt
) {}
