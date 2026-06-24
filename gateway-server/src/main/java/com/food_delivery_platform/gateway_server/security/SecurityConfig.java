package com.food_delivery_platform.gateway_server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors(Customizer.withDefaults())
            .authorizeExchange(exchanges -> exchanges
                // allow actuator health and info without auth
                .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                // allow auth-service endpoints for login/signup without auth through gateway
                .pathMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/signup").permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                // everything else requires authentication
                .anyExchange().authenticated()
            )
            // Configure as OAuth2 Resource Server validating JWTs issued by Keycloak
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
