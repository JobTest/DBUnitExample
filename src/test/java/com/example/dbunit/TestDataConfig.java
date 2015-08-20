package com.example.dbunit;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/application-test.properties")
@Profile("test")
@EnableTransactionManagement
public class TestDataConfig implements DataConfig {

	@Autowired
	private Environment env;

	@Override
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().build();
	}

	@Override
	@Bean
	@Autowired
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();

		Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.put("hibernate.show_sql", false); // avoid double logging
        properties.put("hibernate.format_sql", false);
        properties.put("hibernate.hbm2ddl.auto", "create"); // auto initialization schema in database, based on JPA Entity classes

		bean.setPersistenceProviderClass(HibernatePersistence.class);
		bean.setDataSource(dataSource);
		bean.setJpaProperties(properties);
		bean.setPackagesToScan("com.example.dbunit.domain");
		return bean;
	}

	@Override
	@Bean
	@Autowired
	public JpaTransactionManager transactionManager(EntityManagerFactory emf, DataSource dataSource) {
        JpaTransactionManager bean = new JpaTransactionManager(emf); //JpaTransactionManager bean = new JpaTransactionManager();
        bean.setDataSource(dataSource);
		bean.setJpaDialect(new HibernateJpaDialect());
        //bean.setEntityManagerFactory(emf);
		return bean;
	}

}
