package dev.nano.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class ApiAuthorizationFilter implements GlobalFilter, Ordered {

    @Value("${spring.security.api-key.enabled:true}")
    private boolean apiKeyEnabled;

    final ApiKeyAuthorizationChecker apiKeyAuthorizationChecker;

    @Autowired
    public ApiAuthorizationFilter(
            @Qualifier("main-checker") @Lazy ApiKeyAuthorizationChecker apiKeyAuthorizationChecker
    ) {
        this.apiKeyAuthorizationChecker = apiKeyAuthorizationChecker;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!apiKeyEnabled) {
            return chain.filter(exchange);
        }

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String applicationName = route.getId();
        List<String> apiKey = exchange.getRequest().getHeaders().get("ApiKey");

        if (applicationName == null || Objects.requireNonNull(apiKey).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Application name is not defined, you are not authorized to access this resource"
            );
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
