package ru.clevertec.ecl.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;
import java.util.Properties;

@Configuration
public class ProfileConfig implements EnvironmentAware {

    private Environment environment;

    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        String profile = environment.getProperty("spring.profiles.active");
        PropertySourcesPlaceholderConfigurer propertyConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource[]{
                new ClassPathResource("application.yml"),
                new ClassPathResource(String.format("application-%s.yml", profile))
        });
        Properties yamlObject = Objects.requireNonNull(yaml.getObject(), "Could not load yml");
        propertyConfigurer.setProperties(yamlObject);
        return propertyConfigurer;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
