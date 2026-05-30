package com.watts2crypto.watts2crypto_backend.config;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfig {
    @Value("${app.cors.allowed-origins:}")
    private String allowedOrigins;

    @Value("${app.cors.allowed-local-hosts:localhost,127.0.0.1,::1}")
    private String allowedHosts;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**", "/api/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/api/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
            );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                String origin = request.getHeader("Origin");
                if (origin == null || origin.isBlank()) {
                    return null;
                }

                if (!isAllowedExactOrigin(origin) && !isAllowedLocalOrigin(origin)) {
                    return null;
                }

                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of(origin));
                configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);
                return configuration;
            }
        };
    }

    private boolean isAllowedExactOrigin(String origin) {
        return Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(allowedOrigin -> !allowedOrigin.isEmpty())
                .anyMatch(allowedOrigin -> allowedOrigin.equalsIgnoreCase(origin));
    }

    private boolean isAllowedLocalOrigin(String origin) {
        try {
            URI originUri = URI.create(origin);
            String host = originUri.getHost();
            if (host == null || originUri.getPort() < 0) {
                return false;
            }

            return Arrays.stream(allowedHosts.split(","))
                    .map(String::trim)
                    .map(SecurityConfig::normalizeHost)
                    .filter(allowedHost -> !allowedHost.isEmpty())
                    .anyMatch(allowedHost -> allowedHost.equalsIgnoreCase(normalizeHost(host)));
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static String normalizeHost(String host) {
        String normalized = host.trim();
        if (normalized.startsWith("[") && normalized.endsWith("]")) {
            return normalized.substring(1, normalized.length() - 1);
        }
        return normalized;
    }
}
