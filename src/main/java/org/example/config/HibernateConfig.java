package org.example.config;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.hibernate.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    private final Environment environment;

    public HibernateConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setUrl(
                environment.getRequiredProperty("db.url")
        );

        dataSource.setUser(
                environment.getRequiredProperty("db.username")
        );

        dataSource.setPassword(
                environment.getRequiredProperty("db.password")
        );

        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        sessionFactory.setDataSource(dataSource);

        sessionFactory.setPackagesToScan("org.example.model");

        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();

        properties.setProperty(
                "hibernate.hbm2ddl.auto",
                "update"
        );

        properties.setProperty(
                "hibernate.show_sql",
                "true"
        );

        properties.setProperty(
                "hibernate.format_sql",
                "true"
        );

        properties.setProperty(
                "hibernate.dialect",
                "org.hibernate.dialect.PostgreSQLDialect"
        );

        return properties;
    }
}
