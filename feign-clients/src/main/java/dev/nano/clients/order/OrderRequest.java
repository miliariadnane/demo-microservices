package dev.nano.clients.order;

public record OrderRequest(
        Long customerId,
        String customerName,
        String customerEmail,
        Long productId,
        Integer amount
) {}
