package dev.nano.clients.product;

public record ProductResponse(
        String name,
        String image,
        Integer price
) {}
