package dev.nano.product.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Adds an {@code X-App-Version} response header to identify which deployment
 * version (e.g. "blue" or "green") handled the request.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class VersionHeaderFilter extends OncePerRequestFilter {

    private static final String VERSION_HEADER = "X-App-Version";

    @Value("${app.version:unknown}")
    private String appVersion;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        response.setHeader(VERSION_HEADER, appVersion);
        filterChain.doFilter(request, response);
    }
}
