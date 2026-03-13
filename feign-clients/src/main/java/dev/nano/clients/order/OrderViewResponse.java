package dev.nano.clients.order;

import java.time.LocalDateTime;

public record OrderViewResponse(
        Long orderId,
        Long customerId,
        String customerName,
        String customerEmail,
        Long productId,
        String productName,
        double productPrice,
        double amount,
        LocalDateTime createdAt
) {}
