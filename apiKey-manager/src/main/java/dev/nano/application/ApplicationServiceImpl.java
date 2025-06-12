package dev.nano.application;

import dev.nano.apikey.ApiKeyEntity;
import dev.nano.apikey.ApiKeyRepository;
import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static dev.nano.apikey.ApiKeyConstant.API_KEY_NOT_FOUND;
import static dev.nano.application.ApplicationConstant.APPLICATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService{
    private final ApplicationRepository applicationRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    public void assignApplicationToApiKey(ApplicationName applicationName, String apiKey) {
        ApiKeyEntity key = apiKeyRepository.findByKey(apiKey)
                .orElseThrow(() -> new ResourceNotFoundException(API_KEY_NOT_FOUND));

        ApplicationEntity application = ApplicationEntity.builder()
                    .applicationName(applicationName)
                    .apiKey(key)
                    .revoked(false)
                    .enabled(true)
                    .approved(true)
                    .build();

        applicationRepository.save(application);
    }

    @Override
    public void revokeApplicationFromApiKey(String applicationName, String apiKey) {
        if(!apiKeyRepository.doesKeyExists(apiKey))
            throw new ResourceNotFoundException(API_KEY_NOT_FOUND);

        ApplicationEntity application = applicationRepository.findByApplicationName(applicationName)
                .orElseThrow(() -> new ResourceNotFoundException(APPLICATION_NOT_FOUND));

        application.setRevoked(true);
        application.setEnabled(false);
        application.setApproved(false);

        applicationRepository.save(application);
    }
}
