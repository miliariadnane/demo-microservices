package dev.nano.apikey;

import dev.nano.application.ApplicationName;

public interface ApiKeyService {
    String save(ApiKeyRequest apiKeyRequest);
    void revokeApi(String key);
    boolean isAuthorized(String apiKey, ApplicationName applicationName);
}
