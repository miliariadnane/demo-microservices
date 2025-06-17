package dev.nano.apikey;

import dev.nano.application.ApplicationName;
import org.springframework.transaction.annotation.Transactional;

public interface ApiKeyService {
    String save(ApiKeyRequest apiKeyRequest);
    void revokeApi(String key);
    @Transactional(readOnly = true)
    boolean isAuthorized(String apiKey, ApplicationName applicationName);
}
