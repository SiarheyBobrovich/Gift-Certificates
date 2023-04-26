package ru.clevertec.ecl.util;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

@Testcontainers
public class StaticPostgresTestContainer {

    @Container
    private static final JdbcDatabaseContainer<?> jdbcDatabaseContainer = new PostgreSQLContainer<>("postgres:15.1")
            .withDatabaseName("clevertec")
            .withUsername("postgres")
            .withPassword("172143")
            .withInitScript("postgres_init_script.sql");

    protected SessionFactory sessionFactory() {
        return new Configuration()
                .setProperty("hibernate.connection.driver_class", jdbcDatabaseContainer.getDriverClassName())
                .setProperty("hibernate.connection.url", jdbcDatabaseContainer.getJdbcUrl())
                .setProperty("hibernate.connection.username", jdbcDatabaseContainer.getUsername())
                .setProperty("hibernate.connection.password", jdbcDatabaseContainer.getPassword())
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect")
                .setProperty("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider")
                .setProperty("hibernate.current_session_context_class", "thread")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.format_sql", "true")
                .addAnnotatedClass(Tag.class)
                .addAnnotatedClass(GiftCertificate.class)
                .buildSessionFactory();
    }
}