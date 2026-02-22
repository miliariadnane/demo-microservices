package dev.nano.clients.order;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long customerId,
        Long productId,
        Integer amount,
        LocalDateTime createAt
) {}
