package dev.nano.gateway.filter;

import dev.nano.gateway.security.ApiKeyAuthorizationChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global API Key Authentication Filter
 *
 * This filter implements API key-based authentication for all incoming requests to the gateway.
 * It validates that each request has a valid API key with proper permissions for the target service.
 *
 * Authentication Flow:
 * 1. Extracts API key from X-API-KEY header
 * 2. Determines target service from request path
 * 3. Validates API key authorization for the target service
 * 4. Allows or denies request based on authorization result
 *
 * Usage:
 * - All requests must include X-API-KEY header
 * - API key must have permission for the target service
 * - Invalid or unauthorized keys result in 401 Unauthorized response
 *
 * Example:
 * Request to /api/v1/customers
 * - Extracts 'CUSTOMER' as service name
 * - Validates API key has permission for CUSTOMER service
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthenticationFilter implements GlobalFilter {

    private final ApiKeyAuthorizationChecker apiKeyChecker;
    private static final String API_KEY_HEADER = "X-API-KEY";

    /**
     * Filters incoming requests to validate API key authentication
     *
     * @param exchange the current server exchange
     * @param chain the filter chain
     * @return Mono<Void> representing the completion of request processing
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String apiKey = exchange.getRequest().getHeaders().getFirst(API_KEY_HEADER);
        String applicationName = extractApplicationName(exchange);

        log.debug("Validating API key for application: {}", applicationName);

        if (apiKey == null || !apiKeyChecker.isAuthorized(apiKey, applicationName)) {
            log.warn("Unauthorized access attempt for application: {}. Invalid or missing API key", applicationName);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        log.debug("API key validated successfully for application: {}", applicationName);
        return chain.filter(exchange);
    }

    /**
     * Extracts the application/service name from the request path
     * Example: /api/v1/customers -> CUSTOMER
     *
     * @param exchange the current server exchange
     * @return the uppercase service name
     */
    private String extractApplicationName(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length >= 4 && pathParts[1].equals("api") && pathParts[2].equals("v1")) {
            String serviceName = pathParts[3];
            if (serviceName.endsWith("s")) {
                serviceName = serviceName.substring(0, serviceName.length() - 1);
            }
            return serviceName.toUpperCase();
        }

        log.error("Invalid API path format: {}", path);
        throw new IllegalStateException("Invalid API path format");
    }
}
