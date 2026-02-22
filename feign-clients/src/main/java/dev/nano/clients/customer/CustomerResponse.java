package dev.nano.clients.customer;

public record CustomerResponse(
        Long id,
        String name,
        String email
) {}
