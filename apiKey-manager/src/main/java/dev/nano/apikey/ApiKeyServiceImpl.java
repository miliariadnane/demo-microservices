package dev.nano.apikey;

import dev.nano.application.ApplicationEntity;
import dev.nano.application.ApplicationName;
import dev.nano.application.ApplicationRepository;
import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.nano.apikey.ApiKeyConstant.API_KEY_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {
    private static final Integer EXPIRATION_DAYS = 30;
    private final ApiKeyRepository apiKeyRepository;
    private final ApplicationRepository applicationRepository;
    private final KeyGenerator keyGenerator;

    @Override
    public String save(ApiKeyRequest apiKeyRequest) {
        ApiKeyEntity apiKey = new ApiKeyEntity();

        apiKey.setClient(apiKeyRequest.client());
        apiKey.setDescription(apiKeyRequest.description());

        String apiKeyValue = keyGenerator.generateKey();
        apiKey.setKey(apiKeyValue);

        apiKey.setApproved(true);
        apiKey.setEnabled(true);
        apiKey.setNeverExpires(false);
        apiKey.setCreatedDate(LocalDateTime.now());
        apiKey.setExpirationDate(LocalDateTime.now().plusDays(EXPIRATION_DAYS));

        ApiKeyEntity savedApiKeyEntity = apiKeyRepository.save(apiKey);

        Set<ApplicationEntity> applications = Optional.ofNullable(apiKeyRequest.applications())
                .orElse(List.of())
                .stream().map(app -> ApplicationEntity.builder()
                        .applicationName(app)
                        .apiKey(savedApiKeyEntity)
                        .revoked(false)
                        .enabled(true)
                        .build())
                .collect(Collectors.toUnmodifiableSet());

        applicationRepository.saveAll(applications);

        return apiKeyValue;
    }

    @Override
    public void revokeApi(String key) {
        ApiKeyEntity apiKey = apiKeyRepository.findByKey(key).orElseThrow(
                () -> new ResourceNotFoundException(API_KEY_NOT_FOUND));

        apiKey.setRevoked(true);
        apiKey.setEnabled(false);
        apiKey.setApproved(false);
        apiKeyRepository.save(apiKey);

        // revoke all applications associated with this api key
        apiKey.getApplications().forEach(app -> {
            app.setRevoked(true);
            app.setEnabled(false);
            app.setApproved(false);
            applicationRepository.save(app);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAuthorized(String apiKey, ApplicationName applicationName) {
        log.info("Attempting to authorize API Key: {} for Application: {}", apiKey, applicationName);
        Optional<ApiKeyEntity> optionalApiKey = apiKeyRepository.findByKeyAndApplicationName(apiKey, applicationName);

        if(optionalApiKey.isEmpty()) {
            log.warn("API Key {} not found for application {}. Returning false.", apiKey, applicationName);
            return false;
        }

        ApiKeyEntity apiKeyEntity = optionalApiKey.get();

        return apiKeyEntity.getApplications()
                .stream()
                .filter(app -> app.getApplicationName().equals(applicationName))
                .findFirst()
                .map(app -> app.isEnabled() &&
                            app.isApproved() &&
                            !app.isRevoked() &&
                            apiKeyEntity.isEnabled() &&
                            apiKeyEntity.isApproved() &&
                            !apiKeyEntity.isRevoked() &&
                            (apiKeyEntity.isNeverExpires() || LocalDateTime.now().isBefore(apiKeyEntity.getExpirationDate())) // isAfter used to check if the expiration date is in the future
                )
                .orElse(false);

    }
}
