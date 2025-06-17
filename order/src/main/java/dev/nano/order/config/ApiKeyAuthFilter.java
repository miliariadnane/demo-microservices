package dev.nano.order.config;

import dev.nano.clients.apiKeyManager.apiKey.ApiKeyManagerClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String APPLICATION_NAME = "ORDER";

    private final ApiKeyManagerClient apiKeyManagerClient;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestApiKey = request.getHeader(API_KEY_HEADER);

        if (requestApiKey != null) {
            log.info("Received request with API Key: {}", requestApiKey);
            try {
                boolean isAuthorized = apiKeyManagerClient.isKeyAuthorizedForApplication(requestApiKey, APPLICATION_NAME);
                log.info("API Key '{}' authorization for application '{}': {}", requestApiKey, APPLICATION_NAME, isAuthorized); // Log the authorization result
                if (isAuthorized) {
                    // If the API Key is valid, authenticate the request as an internal service call
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            "internal-service",
                            null,
                            AuthorityUtils.createAuthorityList("ROLE_INTERNAL_SERVICE")
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Successfully authenticated request with API Key.");
                } else {
                    log.warn("API Key '{}' is NOT authorized for application '{}'.", requestApiKey, APPLICATION_NAME);
                }
            } catch (Exception e) {
                log.error("Error during API Key authorization for key '{}' and application '{}': {}", requestApiKey, APPLICATION_NAME, e.getMessage(), e);
            }
        } else {
            log.debug("No X-API-KEY header found in the request.");
        }

        filterChain.doFilter(request, response);
    }
}
