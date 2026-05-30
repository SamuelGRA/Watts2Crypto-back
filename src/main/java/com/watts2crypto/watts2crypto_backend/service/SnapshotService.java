package com.watts2crypto.watts2crypto_backend.service;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SnapshotService {
	private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
	private static final DateTimeFormatter OFFSET_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

	private final DataSource dataSource;
	private final JdbcTemplate jdbcTemplate;

	public SnapshotService(DataSource dataSource, JdbcTemplate jdbcTemplate) {
		this.dataSource = dataSource;
		this.jdbcTemplate = jdbcTemplate;
	}

	public void writeSnapshot(Writer writer) throws SQLException, IOException {
		List<TableSnapshot> tables = listTables();

		writer.write("-- Watts2Crypto database snapshot\n");
		writer.write("SET REFERENTIAL_INTEGRITY FALSE;\n\n");

		for (TableSnapshot table : tables) {
			writer.write("TRUNCATE TABLE ");
			writer.write(quoteQualifiedIdentifier(table.schemaName(), table.tableName()));
			writer.write(";\n");
		}

		writer.write("\n");
		for (TableSnapshot table : tables) {
			writeTableRows(writer, table);
		}

		writer.write("\n");
		for (TableSnapshot table : tables) {
			writeIdentityResets(writer, table);
		}

		writer.write("SET REFERENTIAL_INTEGRITY TRUE;\n");
	}

	public void importSnapshot(MultipartFile snapshotFile) throws IOException, SQLException {
		Path tempFile = Files.createTempFile("watts2crypto-snapshot-", ".sql");
		try {
			Files.writeString(tempFile, new String(snapshotFile.getBytes(), StandardCharsets.UTF_8), StandardCharsets.UTF_8);
			try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
				String scriptPath = tempFile.toAbsolutePath().toString().replace("\\", "/");
				statement.execute("RUNSCRIPT FROM '" + scriptPath.replace("'", "''") + "'");
			}
		} finally {
			Files.deleteIfExists(tempFile);
		}
	}

	private void writeTableRows(Writer writer, TableSnapshot table) throws SQLException, IOException {
		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + quoteQualifiedIdentifier(table.schemaName(), table.tableName()))) {

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<String> columnNames = new ArrayList<>(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				columnNames.add(quoteIdentifier(metaData.getColumnLabel(i)));
			}

			while (resultSet.next()) {
				writer.write("INSERT INTO ");
				writer.write(quoteQualifiedIdentifier(table.schemaName(), table.tableName()));
				writer.write(" (");
				writer.write(String.join(", ", columnNames));
				writer.write(") VALUES (");

				for (int i = 1; i <= columnCount; i++) {
					if (i > 1) {
						writer.write(", ");
					}
					writer.write(formatLiteral(resultSet.getObject(i)));
				}

				writer.write(");\n");
			}
		}
	}

	private void writeIdentityResets(Writer writer, TableSnapshot table) throws SQLException, IOException {
		for (String identityColumn : table.identityColumns()) {
			Long maxValue = jdbcTemplate.queryForObject(
					"SELECT MAX(" + quoteIdentifier(identityColumn) + ") FROM " + quoteQualifiedIdentifier(table.schemaName(), table.tableName()),
					Long.class);

			if (maxValue != null) {
				writer.write("ALTER TABLE ");
				writer.write(quoteQualifiedIdentifier(table.schemaName(), table.tableName()));
				writer.write(" ALTER COLUMN ");
				writer.write(quoteIdentifier(identityColumn));
				writer.write(" RESTART WITH ");
				writer.write(Long.toString(maxValue + 1));
				writer.write(";\n");
			}
		}
	}

	private List<TableSnapshot> listTables() throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			DatabaseMetaData metaData = connection.getMetaData();
			List<TableSnapshot> tables = new ArrayList<>();

			try (ResultSet resultSet = metaData.getTables(null, "%", "%", new String[] { "TABLE" })) {
				while (resultSet.next()) {
					String schemaName = resultSet.getString("TABLE_SCHEM");
					String tableName = resultSet.getString("TABLE_NAME");
					if (!isExportableSchema(schemaName) || tableName == null || tableName.startsWith("SYSTEM_") || tableName.startsWith("INFORMATION_SCHEMA")) {
						continue;
					}

					tables.add(new TableSnapshot(schemaName, tableName, listIdentityColumns(metaData, schemaName, tableName)));
				}
			}

			tables.sort(Comparator.comparing(TableSnapshot::tableName));
			return tables;
		}
	}

	private List<String> listIdentityColumns(DatabaseMetaData metaData, String schemaName, String tableName) throws SQLException {
		List<String> identityColumns = new ArrayList<>();
		try (ResultSet columns = metaData.getColumns(null, schemaName, tableName, "%")) {
			while (columns.next()) {
				String isAutoincrement = columns.getString("IS_AUTOINCREMENT");
				if ("YES".equalsIgnoreCase(isAutoincrement)) {
					identityColumns.add(columns.getString("COLUMN_NAME"));
				}
			}
		}
		return identityColumns;
	}

	private boolean isExportableSchema(String schemaName) {
		if (schemaName == null) {
			return false;
		}

		String normalizedSchema = schemaName.trim();
		return !normalizedSchema.isEmpty()
				&& !"INFORMATION_SCHEMA".equalsIgnoreCase(normalizedSchema)
				&& !"PG_CATALOG".equalsIgnoreCase(normalizedSchema)
				&& !"SYS".equalsIgnoreCase(normalizedSchema)
				&& !"SYSTEM_LOBS".equalsIgnoreCase(normalizedSchema)
				&& !normalizedSchema.startsWith("SYSTEM_");
	}

	private String formatLiteral(Object value) {
		if (value == null) {
			return "NULL";
		}
		if (value instanceof Boolean booleanValue) {
			return booleanValue ? "TRUE" : "FALSE";
		}
		if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double || value instanceof BigDecimal) {
			return value.toString();
		}
		if (value instanceof LocalDate localDate) {
			return "DATE '" + localDate + "'";
		}
		if (value instanceof java.sql.Date sqlDate) {
			return "DATE '" + sqlDate.toLocalDate() + "'";
		}
		if (value instanceof LocalTime localTime) {
			return "TIME '" + localTime + "'";
		}
		if (value instanceof java.sql.Time sqlTime) {
			return "TIME '" + sqlTime.toLocalTime() + "'";
		}
		if (value instanceof LocalDateTime localDateTime) {
			return "TIMESTAMP '" + localDateTime.format(LOCAL_DATE_TIME_FORMATTER) + "'";
		}
		if (value instanceof Timestamp timestamp) {
			return "TIMESTAMP '" + timestamp.toLocalDateTime().format(LOCAL_DATE_TIME_FORMATTER) + "'";
		}
		if (value instanceof Instant instant) {
			return "TIMESTAMP WITH TIME ZONE '" + instant.atOffset(ZoneOffset.UTC).format(OFFSET_DATE_TIME_FORMATTER) + "'";
		}
		if (value instanceof Enum<?> enumValue) {
			return quoteString(enumValue.name());
		}
		if (value instanceof byte[] bytes) {
			return "X'" + HexFormat.of().formatHex(bytes) + "'";
		}
		return quoteString(value.toString());
	}

	private String quoteString(String value) {
		return "'" + value.replace("'", "''") + "'";
	}

	private String quoteIdentifier(String identifier) {
		return '"' + identifier.replace("\"", "\"\"") + '"';
	}

	private String quoteQualifiedIdentifier(String schemaName, String tableName) {
		return quoteIdentifier(schemaName) + "." + quoteIdentifier(tableName);
	}

	private record TableSnapshot(String schemaName, String tableName, List<String> identityColumns) {
	}
}