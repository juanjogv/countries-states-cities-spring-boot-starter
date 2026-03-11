package com.csc.autoconfigure;

import com.csc.config.CscDatasource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.JournalMode;
import org.sqlite.SQLiteConfig.Pragma;
import org.sqlite.SQLiteConfig.SynchronousMode;
import org.sqlite.SQLiteConfig.TempStore;

@Configuration(proxyBeanMethods = false)
public class CscDataSourceConfiguration {

    // Cover the full ~105 MB database with room to spare
    private static final long MMAP_SIZE = 256L * 1024 * 1024;

    // 64 MB page cache (negative value = kilobytes in SQLite pragma)
    private static final int CACHE_SIZE_KB = -65536;

    @Bean(defaultCandidate = false)
    @CscDatasource
    DataSource cscDataSource() throws IOException {
        Path dbFile = extractDatabase();

        SQLiteConfig sqLiteConfig = new SQLiteConfig();
        sqLiteConfig.setReadOnly(true);
        sqLiteConfig.setJournalMode(JournalMode.OFF);
        sqLiteConfig.setSynchronous(SynchronousMode.OFF);
        sqLiteConfig.setCacheSize(CACHE_SIZE_KB);
        sqLiteConfig.setTempStore(TempStore.MEMORY);
        sqLiteConfig.setPragma(Pragma.MMAP_SIZE, String.valueOf(MMAP_SIZE));

        // immutable=1: SQLite skips all file locking and change detection —
        // the single biggest win for a static, never-written database.
        String jdbcUrl = "jdbc:sqlite:file:" + dbFile.toAbsolutePath() + "?immutable=1";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName("org.sqlite.JDBC");
        config.setDataSourceProperties(sqLiteConfig.toProperties());
        config.setReadOnly(true);
        // With immutable=1 there is no file lock, so connections truly run in parallel.
        config.setMaximumPoolSize(Math.max(4, Runtime.getRuntime().availableProcessors()));
        config.setConnectionTestQuery("SELECT 1");

        return new HikariDataSource(config);
    }

    @Bean(defaultCandidate = false)
    @CscDatasource
    JdbcTemplate cscJdbcTemplate(@CscDatasource DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Copies world.sqlite3 out of the JAR into a temp file so SQLite's native
     * layer can open it by filesystem path.
     */
    private Path extractDatabase() throws IOException {
        ClassPathResource resource = new ClassPathResource("world.sqlite3");
        Path tempFile = Files.createTempFile("csc-world-", ".sqlite3");
        tempFile.toFile().deleteOnExit();
        try (InputStream in = resource.getInputStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile;
    }
}
