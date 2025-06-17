package dev.nano.clients.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InternalApiKeyFeignConfig {

    @Value("${internal.api-key}")
    private String internalApiKey;

    private static final String API_KEY_HEADER = "X-API-KEY";

    @Bean
    public RequestInterceptor internalApiKeyRequestInterceptor() {
        return requestTemplate -> requestTemplate.header(API_KEY_HEADER, internalApiKey);
    }
} 