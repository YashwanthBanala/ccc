package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import io.jsonwebtoken.*;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter jwtAuthFilter() {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            // Public routes — skip JWT
            if (path.startsWith("/auth")) {
                return chain.filter(exchange);
            }

            // Check Authorization header
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorized(exchange);
            }

            try {
                String token = authHeader.substring(7);
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
                return chain.filter(exchange);
            } catch (JwtException e) {
                return unauthorized(exchange);
            }
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
