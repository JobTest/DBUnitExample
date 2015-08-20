package com.example.dbunit;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public interface DataConfig {
	public DataSource dataSource();

	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource);

	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory, DataSource dataSource);

}
