package com.watts2crypto.watts2crypto_backend.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class SnapshotServiceTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private Connection connection;

    @Mock
    private DatabaseMetaData databaseMetaData;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet tablesResultSet;

    @Mock
    private ResultSet columnsResultSet;

    @Mock
    private ResultSet dataResultSet;

    @Mock
    private ResultSetMetaData dataResultSetMetaData;

    @InjectMocks
    private SnapshotService service;

    @Test
    void writeSnapshotGeneratesInsertStatementsAndIdentityResets() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(databaseMetaData);
        when(databaseMetaData.getTables(null, "%", "%", new String[] { "TABLE" })).thenReturn(tablesResultSet);
        when(tablesResultSet.next()).thenReturn(true, true, false);
        when(tablesResultSet.getString("TABLE_SCHEM")).thenReturn("PUBLIC", "SYSTEM_SCHEMA");
        when(tablesResultSet.getString("TABLE_NAME")).thenReturn("wallets", "ignored_table");
        when(databaseMetaData.getColumns(null, "PUBLIC", "wallets", "%")).thenReturn(columnsResultSet);
        when(columnsResultSet.next()).thenReturn(true, false);
        when(columnsResultSet.getString("IS_AUTOINCREMENT")).thenReturn("YES");
        when(columnsResultSet.getString("COLUMN_NAME")).thenReturn("id");
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT * FROM wallets")).thenReturn(dataResultSet);
        when(dataResultSet.getMetaData()).thenReturn(dataResultSetMetaData);
        when(dataResultSetMetaData.getColumnCount()).thenReturn(11);
        when(dataResultSetMetaData.getColumnLabel(anyInt())).thenAnswer(invocation -> switch ((int) invocation.getArgument(0)) {
            case 1 -> "col_null";
            case 2 -> "col_bool";
            case 3 -> "col_int";
            case 4 -> "col_date";
            case 5 -> "col_time";
            case 6 -> "col_localdatetime";
            case 7 -> "col_timestamp";
            case 8 -> "col_instant";
            case 9 -> "col_enum";
            case 10 -> "col_bytes";
            case 11 -> "col_text";
            default -> "col_" + invocation.getArgument(0);
        });
        Object[] values = new Object[] {
                null,
                Boolean.TRUE,
                7,
                LocalDate.of(2026, 5, 30),
                LocalTime.of(12, 34, 56),
                LocalDateTime.of(2026, 5, 30, 12, 34, 56, 123000000),
                Timestamp.valueOf(LocalDateTime.of(2026, 5, 30, 12, 34, 56, 123000000)),
                Instant.parse("2026-05-30T12:34:56Z"),
                com.watts2crypto.watts2crypto_backend.models.Software.HardwareUsable.GPU,
                new byte[] { 0x0a, 0x0b },
                "O'Reilly"
        };
        when(dataResultSet.next()).thenReturn(true, false);
        when(dataResultSet.getObject(anyInt())).thenAnswer(invocation -> values[(int) invocation.getArgument(0) - 1]);
        when(jdbcTemplate.queryForObject("SELECT MAX(id) FROM wallets", Long.class)).thenReturn(7L);

        StringWriter writer = new StringWriter();

        service.writeSnapshot(writer);

        String snapshot = writer.toString();
        assertTrue(snapshot.contains("SET REFERENTIAL_INTEGRITY FALSE;"));
        assertTrue(snapshot.contains("TRUNCATE TABLE wallets;"));
        assertTrue(snapshot.contains("INSERT INTO wallets (col_null, col_bool, col_int, col_date, col_time, col_localdatetime, col_timestamp, col_instant, col_enum, col_bytes, col_text) VALUES (NULL, TRUE, 7, DATE '2026-05-30', TIME '12:34:56', TIMESTAMP '2026-05-30 12:34:56.123000000', TIMESTAMP '2026-05-30 12:34:56.123000000', TIMESTAMP WITH TIME ZONE '2026-05-30T12:34:56Z', 'GPU', X'0a0b', 'O''Reilly');"));
        assertTrue(snapshot.contains("ALTER TABLE wallets ALTER COLUMN id RESTART WITH 8;"));
        assertTrue(snapshot.contains("SET REFERENTIAL_INTEGRITY TRUE;"));

        verify(databaseMetaData).getTables(null, "%", "%", new String[] { "TABLE" });
        verify(statement).executeQuery("SELECT * FROM wallets");
        verify(jdbcTemplate).queryForObject("SELECT MAX(id) FROM wallets", Long.class);
    }

    @Test
    void importSnapshotExecutesRunScriptAndDeletesTempFile() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        MockMultipartFile multipartFile = new MockMultipartFile(
                "snapshot",
                "snapshot.sql",
                "text/plain",
                "SELECT 1;".getBytes());

        service.importSnapshot(multipartFile);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(statement).execute(sqlCaptor.capture());
        assertTrue(sqlCaptor.getValue().startsWith("RUNSCRIPT FROM '"));
        assertTrue(sqlCaptor.getValue().contains(".sql'"));
    }

    @Test
    void writeSnapshotPropagatesSqlFailures() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(databaseMetaData);
        when(databaseMetaData.getTables(null, "%", "%", new String[] { "TABLE" })).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> service.writeSnapshot(new StringWriter()));
    }
}