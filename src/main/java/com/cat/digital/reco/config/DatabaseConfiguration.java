package com.cat.digital.reco.config;

import java.util.Properties;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@TestConfiguration
@Configuration
@EnableJpaRepositories(basePackages = "com.cat.digital.reco.repositories",
    entityManagerFactoryRef = "recoEntityManagerFactory",
    transactionManagerRef = "recoTransactionManager")
public class DatabaseConfiguration {

  @Autowired
  Environment env;

  @Bean
  @Primary
  public DataSource recoDataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(env.getProperty("spring.datasource.url"));
    config.setUsername(env.getProperty("spring.datasource.username"));
    config.setPassword(env.getProperty("spring.datasource.password"));
    config.setPoolName("RecoJPAHikariCP");
    return new HikariDataSource(config);
  }

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean recoEntityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(recoDataSource());
    em.setPackagesToScan("com.cat.digital.reco.domain.entities");
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(additionalProperties());

    return em;
  }

  @Bean
  @Primary
  @Qualifier("reco")
  public PlatformTransactionManager recoTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(recoEntityManagerFactory().getObject());
    return transactionManager;
  }

  final Properties additionalProperties() {
    final Properties hibernateProperties = new Properties();

    hibernateProperties.put("hibernate.physical_naming_strategy", PhysicalNamingStrategyStandardImpl.class.getName());
    hibernateProperties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
    hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "false");
    return hibernateProperties;
  }

}
