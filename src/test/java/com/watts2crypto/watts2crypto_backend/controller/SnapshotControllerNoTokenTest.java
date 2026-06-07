package com.watts2crypto.watts2crypto_backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.watts2crypto.watts2crypto_backend.service.SnapshotService;

@WebMvcTest(value = SnapshotController.class, properties = "refresh.token=")
@ActiveProfiles("dev")
@AutoConfigureMockMvc(addFilters = false)
class SnapshotControllerNoTokenTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SnapshotService snapshotService;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void rejectsExportWhenRefreshTokenIsNotConfigured() throws Exception {
        mockMvc.perform(get("/api/snapshot/export"))
                .andExpect(status().isForbidden());
    }
}
