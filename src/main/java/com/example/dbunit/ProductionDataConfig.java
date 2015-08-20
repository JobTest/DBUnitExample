package com.example.dbunit;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/application-prod.properties")
@Profile("prod")
@ComponentScan(basePackageClasses = ApplicationConfig.class)
public class ProductionDataConfig implements DataConfig {

	@Autowired
	private Environment env;

	@Override
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));
		return dataSource;
	}

	@Override
	@Bean
	@Autowired
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();

		Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.format_sql", false);

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
