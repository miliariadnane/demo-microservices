package dev.nano.gateway.security;

import dev.nano.clients.apiKeyManager.apiKey.ApiKeyManagerClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Service responsible for checking API key authorization against the apiKey-manager service.
 *
 * Features:
 * - Caches authorization results to improve performance
 * - Uses circuit breaker pattern for resilience
 * - Integrates with apiKey-manager service via Feign client
 * - Provides fallback mechanism for service unavailability
 *
 * Authorization Flow:
 * 1. Check cache for existing authorization decision
 * 2. If not in cache, call apiKey-manager service
 * 3. Cache the result for future requests
 * 4. Handle service failures with circuit breaker
 */

@Service("main-checker")
@AllArgsConstructor
@Slf4j
public class ApiKeyManagerAuthorizationChecker implements ApiKeyAuthorizationChecker {

    private final ApiKeyManagerClient apiKeyManagerClient;

    /**
     * Checks if an API key is authorized for a specific application
     *
     * @param apiKey the API key to validate
     * @param applicationName the target application/service name
     * @return true if authorized, false otherwise
     */

    @Override
    @Cacheable(value = "apikey-authorizations", key = "#apiKey + '-' + #applicationName")
    @CircuitBreaker(name = "apiKeyAuthorization", fallbackMethod = "fallbackIsAuthorized")
    public boolean isAuthorized(String apiKey, String applicationName) {
        return apiKeyManagerClient.isKeyAuthorizedForApplication(
                apiKey,
                applicationName
        );
    }

    /**
     * Fallback method when apiKey-manager service is unavailable
     * Returns false to maintain security in case of service failure
     *
     * @param apiKey the API key that was being checked
     * @param applicationName the target application
     * @param ex the exception that triggered the fallback
     * @return false to deny access when service is unavailable
     */
    private boolean fallbackIsAuthorized(String apiKey, String applicationName, Exception ex) {
        log.error("Failed to check API key authorization for key: {} and application: {}. Error: {}",
                apiKey, applicationName, ex.getMessage());
        return false;
    }
}
