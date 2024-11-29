package dev.nano.gateway.security;

public interface ApiKeyAuthorizationChecker {
    boolean isAuthorized(String apiKey, String applicationName);
}
