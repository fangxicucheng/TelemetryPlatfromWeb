package com.fang.config.jpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "mysqlDataSource")
    @Qualifier("mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysqlsource")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }
    @Bean(name = "postgresqlDataSource")
    @Qualifier("postgresqlDataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource.postgresqlsource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

}
