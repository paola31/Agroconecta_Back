package com.agroconecta.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionVerifier implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionVerifier.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseConnectionVerifier(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        String databaseName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        Integer productCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM productos", Integer.class);

        logger.info("Conexion MySQL OK. Base actual: {}. Productos registrados: {}", databaseName, productCount);
    }
}
