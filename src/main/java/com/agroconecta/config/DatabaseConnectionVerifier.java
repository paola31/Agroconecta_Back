package com.agroconecta.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Component
public class DatabaseConnectionVerifier implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionVerifier.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseConnectionVerifier(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        // Verifica al iniciar que la aplicacion puede consultar MySQL mediante la conexion JDBC configurada por Spring
        String databaseName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        Integer productCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM productos", Integer.class);
        Integer stockCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM stock", Integer.class);

        logger.info("Conexion MySQL OK. Base actual: {}. Productos: {}. Registros de stock: {}", databaseName, productCount, stockCount);
    }
}
