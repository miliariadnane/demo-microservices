package dev.nano.clients.product;

public record ProductResponse(
        Long id,
        String name,
        String image,
        Integer price,
        Integer availableQuantity
) {}
