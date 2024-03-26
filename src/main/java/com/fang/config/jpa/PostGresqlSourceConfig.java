package com.fang.config.jpa;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryPostgresql",
        transactionManagerRef="transactionManagerPostgresql",
        basePackages= { "com.fang.database.postgresql.repository" }) //设置Repository所在位置
public class PostGresqlSourceConfig {

    @Autowired
    @Qualifier("postgresqlDataSource")
    private DataSource postgresqlDataSource;

    @Bean(name = "entityManagerPostgres")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryPostgresql(builder).getObject().createEntityManager();
    }
    @Resource
    private HibernateProperties hibernateProperties;
    @Resource
    private JpaProperties jpaProperties;

    @Bean(name = "entityManagerFactoryPostgresql")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPostgresql (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(postgresqlDataSource)
                .properties(getHibernateProperties())
                .packages("com.fang.database.postgresql.entity")         //设置实体类所在位置
                .persistenceUnit("postgresqlPersistenceUnit")
                .build();
    }


    @Bean(name = "transactionManagerPostgresql")
    PlatformTransactionManager transactionManagerPostgresql(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryPostgresql(builder).getObject());
    }
    private Map<String, Object> getHibernateProperties() {
        System.out.println("开始了");
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
    }
}
