package com.watts2crypto.watts2crypto_backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

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
@ActiveProfiles("dev")
@AutoConfigureMockMvc(addFilters = false)
class SnapshotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SnapshotService snapshotService;

    @MockitoBean
    private RestTemplateBuilder restTemplateBuilder;

    @Test
    void exportsSnapshotWhenTokenMatches() throws Exception {
        doAnswer(invocation -> {
            Writer writer = invocation.getArgument(0);
            writer.write("snapshot-content");
            return null;
        }).when(snapshotService).writeSnapshot(any(Writer.class));

        mockMvc.perform(get("/api/snapshot/export").header("X-Refresh-Token", "test-token"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("watts2crypto-snapshot.sql")))
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string("snapshot-content"));
    }

    @Test
    void rejectsExportWithWrongToken() throws Exception {
        mockMvc.perform(get("/api/snapshot/export").header("X-Refresh-Token", "wrong-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void exportsSnapshotWhenBearerTokenMatches() throws Exception {
        doAnswer(invocation -> {
            Writer writer = invocation.getArgument(0);
            writer.write("snapshot-content");
            return null;
        }).when(snapshotService).writeSnapshot(any(Writer.class));

        mockMvc.perform(get("/api/snapshot/export").header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("watts2crypto-snapshot.sql")));
    }

    @Test
    void importsSnapshotLocally() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "snapshot.sql", "text/plain",
                "SELECT 1;".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/snapshot/import").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));

        verify(snapshotService).importSnapshot(any());
    }

    @Test
    void importsSnapshotReturnsServerErrorWhenServiceFails() throws Exception {
        doThrow(new SQLException("boom")).when(snapshotService).importSnapshot(any());

        MockMultipartFile file = new MockMultipartFile("file", "snapshot.sql", "text/plain",
                "SELECT 1;".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/snapshot/import").file(file))
                .andExpect(status().isInternalServerError());
    }
}