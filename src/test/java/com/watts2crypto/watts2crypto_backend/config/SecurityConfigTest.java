package com.watts2crypto.watts2crypto_backend.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfiguration;

class SecurityConfigTest {

    @Test
    void returnsCorsConfigurationForAllowedExactOrigin() {
        SecurityConfig securityConfig = new SecurityConfig();
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "https://frontend.example.com");
        ReflectionTestUtils.setField(securityConfig, "allowedHosts", "localhost,127.0.0.1,::1");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Origin", "https://frontend.example.com");

        CorsConfiguration configuration = securityConfig.corsConfigurationSource().getCorsConfiguration(request);

        assertThat(configuration).isNotNull();
        assertThat(configuration.getAllowedOrigins()).containsExactly("https://frontend.example.com");
        assertThat(configuration.getAllowedMethods()).containsExactly("GET", "POST", "OPTIONS");
        assertThat(configuration.getAllowCredentials()).isTrue();
    }

    @Test
    void returnsCorsConfigurationForAllowedLocalOrigin() {
        SecurityConfig securityConfig = new SecurityConfig();
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "https://frontend.example.com");
        ReflectionTestUtils.setField(securityConfig, "allowedHosts", "localhost,127.0.0.1,::1");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Origin", "http://localhost:3000");

        CorsConfiguration configuration = securityConfig.corsConfigurationSource().getCorsConfiguration(request);

        assertThat(configuration).isNotNull();
        assertThat(configuration.getAllowedOrigins()).containsExactly("http://localhost:3000");
    }

    @Test
    void returnsNullForDisallowedOrigin() {
        SecurityConfig securityConfig = new SecurityConfig();
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", "https://frontend.example.com");
        ReflectionTestUtils.setField(securityConfig, "allowedHosts", "localhost,127.0.0.1,::1");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Origin", "https://evil.example.com");

        CorsConfiguration configuration = securityConfig.corsConfigurationSource().getCorsConfiguration(request);

        assertThat(configuration).isNull();
    }
}