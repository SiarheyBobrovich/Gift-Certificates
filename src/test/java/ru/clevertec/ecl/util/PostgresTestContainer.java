package ru.clevertec.ecl.util;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresTestContainer {

    private static final PostgreSQLContainer<?> jdbcDatabaseContainer =
            new PostgreSQLContainer<>("postgres:15.1");

    @BeforeAll
    static void start() {
        jdbcDatabaseContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", jdbcDatabaseContainer::getJdbcUrl);
    }
}
