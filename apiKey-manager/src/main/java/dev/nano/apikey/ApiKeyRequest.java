package dev.nano.apikey;

import dev.nano.application.ApplicationName;

import java.util.List;

public record ApiKeyRequest(
    String client,
    String description,
    List<ApplicationName> applications
) {
}
