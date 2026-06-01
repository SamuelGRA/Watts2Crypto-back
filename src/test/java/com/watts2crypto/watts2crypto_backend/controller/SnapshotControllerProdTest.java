package com.watts2crypto.watts2crypto_backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.watts2crypto.watts2crypto_backend.service.SnapshotService;

@WebMvcTest(value = SnapshotController.class, properties = "refresh.token=test-token")
@ActiveProfiles("prod")
@AutoConfigureMockMvc(addFilters = false)
class SnapshotControllerProdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SnapshotService snapshotService;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void rejectsImportInProdProfile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "snapshot.sql", "text/plain",
                "SELECT 1;".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/snapshot/import").file(file))
                .andExpect(status().isForbidden());
    }
}
