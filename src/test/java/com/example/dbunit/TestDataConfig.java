package com.example.dbunit;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 ** {@link http://devcolibri.com/3966}
 *** {@link https://github.com/wizardjedi/my-spring-learning/wiki/Работа-с-базами-данных-на-основе-jpa}
 * {@link http://devcolibri.com/3575}  {@link http://habrahabr.ru/post/140658/}  {@link https://github.com/springtestdbunit/spring-test-dbunit}
 * Разберем аннотации DBUnit:
 * @Configuration — говорит, что данный класс является Spring конфигурацией;
 * @EnableTransactionManagement — включает TransactionManager для управления транзакциями БД; (это фабрика-менеджеров)
 * @ComponentScan("com.devcolibri.dataexam") — указываем Spring где нужно искать Entity, DAO, Service и т.п.; (по сути это где определено AbstractAplicationContext...)
 * @ContextConfiguration(classes = { ApplicationConfig.class }) — указываем Spring где нужно искать Entity, DAO, Service и т.п.; (по сути это где определено AbstractAplicationContext...)
 * @PropertySource("classpath:app.properties") — подключаем файл свойств созданный выше;
 * ( @EnableJpaRepositories("com.devcolibri.dataexam.repository") — включаем возможность использования JPARepository и говорим, где их искать. (это DAO) )
 *
 * @Repository - это уже реализация DAO
 * @EnableTransactionManagement - это фабрика-менеджеров для DAO
 *
 * Дело в том что 'DBUnit' (наследуется от DBTestCase,JdbcBasedDBTestCase) имеет собственные спец.-классы которые уже умеют выполнять полное авто-тестирование базы и ее таблиц (IDataSet,IDatabaseTester), который является контейнером для сущности в несколько жизненных этапов...
 * Сам же Spring имеет шаблоны (JpaTemplate,HibernateTemplate,JdbcTemplate) которые являются обверткой над JPA,Hibernate,JDBC. То есть:
 * по сути наш DAO уже ненужно реализовывать, мы просто говорим что он является реализацией шаблона (JpaTemplate,HibernateTemplate,JdbcTemplate)
 *
 * И '@Transaction' стоит внутри класса-сервиса - который либо подтверждает либо откатывает список множеств запросов...
 */
@Configuration
@PropertySource("classpath:/application-test.properties")
@Profile("test")
@EnableTransactionManagement
public class TestDataConfig implements DataConfig {

	@Autowired
	private Environment env; // (нужен для возможности получать свойства из property файла)

    // <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    //     <property name="driverClassName" value="com.mysql.jdbc.Driver" />
    //     <property name="url" value="jdbc:mysql://localhost/test" />
    //     <property name="username" value="root" />
    //     <property name="password" value="123654789" />
    // </bean>
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

    // <bean id="emf" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
    //     <property name="dataSource" ref="dataSource" />
    //     <property name="packagesToScan" value="a1s.learn" />
    //     <property name="jpaVendorAdapter">
    //         <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
    //             <property name="showSql" value="true" />
    //             <property name="database" value="MYSQL" />
    //             <property name="databasePlatform" value="org.hibernate.dialect.MySQL5Dialect" />
    //         </bean>
    //     <property name="databasePlatform" value="org.hibernate.dialect.MySQL5Dialect" />
    // </bean>
    //
    // <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager" />
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
