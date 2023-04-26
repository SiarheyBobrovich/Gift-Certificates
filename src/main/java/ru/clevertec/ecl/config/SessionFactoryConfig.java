package ru.clevertec.ecl.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

@Configuration
public class SessionFactoryConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.hibernate.dialect}")
    private String dialect;

    @Value("${spring.hibernate.connection.driver}")
    private String driver;

    @Value("${spring.hibernate.show_sql}")
    private String showSql;

    @Value("${spring.hibernate.format_sql}")
    private String formatSql;

    @Value("${spring.hibernate.hikari.maximumPoolSize}")
    private String maximumPoolSize;

    @Bean
    public SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration()
                .setProperty("hibernate.connection.driver_class", driver)
                .setProperty("hibernate.connection.url", url)
                .setProperty("hibernate.connection.username", username)
                .setProperty("hibernate.connection.password", password)
                .setProperty("hibernate.dialect", dialect)
                .setProperty("hibernate.show_sql", showSql)
                .setProperty("hibernate.format_sql", formatSql)
                .setProperty("hibernate.hikari.maximumPoolSize", maximumPoolSize)
                .setProperty("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider")
                .setProperty("hibernate.current_session_context_class", "thread")
                .addAnnotatedClass(Tag.class)
                .addAnnotatedClass(GiftCertificate.class)
                .buildSessionFactory();
    }
}
