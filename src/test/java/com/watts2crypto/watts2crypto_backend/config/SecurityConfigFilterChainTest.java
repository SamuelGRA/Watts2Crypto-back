package com.watts2crypto.watts2crypto_backend.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class SecurityConfigFilterChainTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void apiPathsRemainAccessibleThroughSecurityFilterChain() throws Exception {
        mockMvc.perform(get("/api/does-not-exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    void nonPermittedPathsAreProtectedBySecurityFilterChain() throws Exception {
        mockMvc.perform(get("/secure-area"))
                .andExpect(status().isForbidden());
    }
}