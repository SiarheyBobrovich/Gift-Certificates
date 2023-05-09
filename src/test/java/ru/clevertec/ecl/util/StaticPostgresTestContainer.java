package ru.clevertec.ecl.util;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class StaticPostgresTestContainer {

    @Container
    private static final PostgreSQLContainer<?> jdbcDatabaseContainer = new PostgreSQLContainer<>("postgres:15.1");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", jdbcDatabaseContainer::getJdbcUrl);
    }
}
