package ru.zettatech.apigateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
@Order(1)
public class JwtAuthenticationFilter implements WebFilter {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.prefix:Bearer }")
    private String jwtPrefix;

    // Paths that don't require authentication
    private static final List<String> OPEN_API_PATHS = List.of(
            "/api/auth/signup",
            "/api/auth/signin",
            "/actuator",
            "/health"
    );

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();

        // Skip authentication for open paths
        if (isOpenPath(path)) {
            return chain.filter(exchange);
        }

        // Check for Authorization header
        if (request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION) == null) {
            log.debug("No Authorization header for path: {}", path);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // Validate Bearer token format
        if (authHeader == null || !authHeader.startsWith(jwtPrefix)) {
            log.debug("Invalid Authorization header format for path: {}", path);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        String token = authHeader.substring(jwtPrefix.length()).trim();

        try {
            // Validate JWT token
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            // Token is valid, extract user info and add to headers for downstream services
            String username = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

            request.mutate()
                    .header("X-User-Name", username)
                    .header("X-Gateway-Authenticated", "true");

            return chain.filter(exchange);
        } catch (Exception e) {
            log.warn("Invalid JWT token for path {}: {}", path, e.getMessage());
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    private boolean isOpenPath(String path) {
        return OPEN_API_PATHS.stream().anyMatch(path::startsWith);
    }
}
