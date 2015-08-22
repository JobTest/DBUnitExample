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
 *
 * **************************************************************************************[ 1 ]
 * Работа со 'Spring' включает два ключевых подхода:
 * 1. DI/IoC (Депенденци Инжекшин или Инвершин Контроль) - например:
 *    Есть менеджер, который будет возвращать модуль собраный из пакета структурно-связаных классов.
 *    В результате мы получим только некий конкретный/общий тип-объекта (но не все его внутренние классы из которых он состоит).
 *    В случае тестирования отдельного внутреннего класса такого модуля - невозможно протестировать один отдельно взятый внутренний класс...
 *       потому-что менеджер может вернуть только конкретный/общий тип-объекта (но не его внутренние классы).
 *    Возможность сперва генерить/создавать внутренние классы модуля и только потом из них собирать сам модуль (общий тип-объекта) - это и есть инвертирование контроля!
 * 2. AOP (Аспектно Ориентировнное программирование) - это сохранение и передача сквозной функциональности, например:
 *    Существует определенный способ реализации и организации работы и интнрфейсом JDBC или объектно-реляционной-моделью JPA/Hibernate.
 *    Каждый из этих способов обладает конкретными функциональными способностями, которые присущи только для конкретной технологии.
 *    Можно реализовать некий класс-обвертку, такой себе шаблон (в Spring-е это: JdbcRepository/JpaRepository/HibernateRepository)
 *       который полностью будет наследовать/поддерживать функционал для конкретной технологии.
 *    Так-вот, класс-обвертка и технология реализована вендором-поставщиком - это абсолютно разные в плане способа реализации вещи, внутри
 *       используются принцыпиально-разные подход работы с технологией (XML и наследование), но общая функциональность между ними сохраняеться!
 *       К примеру аннотации позволяют придать/наделить классы и методы определенным функционалом и при этом незаботясь о его реализации...
 *
 * При организации работы с базой данных вендеры используют явные сессии (PreparedStatement, Hibernate - Session).
 * Spring для работы с подобными сессиями к базе данных использует '@Transaction' - такая себе обвертка, коорая реализует работу с множеством сессий...
 *
 * **************************************************************************************[ 2 ]
 * {@link http://java-course.ru/student/book1/servlet/}
 * {@link http://www.javaportal.ru/java/articles/java_http_web/article05.html}
 * {@link http://devcolibri.com/4284}
 * {@link http://www.technerium.ru/tehnologiya-java-servlet/kak-napisat-prostoy-servlet-v-ide-eclipse-tomcat-v7}
 * {@link http://crypto.pp.ua/2010/06/seans-sessiya-v-java/}
 * {@link http://javatutor.net/books/tiej/socket}
 * {@link http://www.ibm.com/developerworks/ru/library/wa-reverseajax4/}
 * {@link https://netbeans.org/kb/docs/web/ajax-quickstart_ru.html}
 * {@link http://habrahabr.ru/company/xakep/blog/244477/} {@link http://adyadchenko.blogspot.com/2012/11/vaadin.html}
 * {@link http://alextretyakov.blogspot.com/2011/10/spring-gwt-2-gwt.html}  {@link http://dmitrynikol.blogspot.com/2011/08/json-gwt_08.html} {@link http://habrahabr.ru/post/94844/}  {@link https://netbeans.org/kb/74/web/quickstart-webapps-gwt_ru.html}
 *
 * [ Сокет ]
 * Все (веб)приложения связываются между собой по компютерной сети с помощью 'TCP/IP' протокола.
 * Это протокол нижнего уровня, который позволяет определить адресс (веб)приложения:
 * - 'маска подсети' - ограничивает доступные типы сетевых соединений (локальная/корпоративная/глобальная/...)
 * - IP-адресс компьютера в сети (xxx.xxx.xxx.xxx)
 * - 'порт' - это адресс внутри компютера под которым доступно/работает (веб)приложение
 * 'Сокет' предоставляет абстрактный терминал, который подключается к порту на котором доступно/работает (веб)приложение или любое приложение.
 *    Этот терминал, после подключения к порту..., потом прослушиаает клиентские обращения к нему и перенаправляет на (веб)приложение.
 * [ HTTP ]
 * 'HTTP' - это протокол для передачи гипертекста ('гипертекст' - это обычный текст, который благодаря тегам превращается в гипертекст при отображении его веб-браузером).
 * 'HTTP' - состоит из: запроса ('Request' - от клиента на сервер) и ответа ('Response' от сервера на клиент)
 * (текстовый) HTTP-протокол имеет:
 * - 'тип запроса' (их всего 7, которые используются для передачи данных: DELETE/HEAD/GET/OPTIONS/POST/PUT/TRACE)
 * - 'форму запроса' (GET <URL> HTTP/1.0 <имя_заголовка>:<значение_заголовка>):
 * -- тип запросы, который определяет функцию которая должна выполняться на сервере (читать/добаввить/изменить/удалить)
 * -- IP-адресс/порт серверного (веб)приложения (и дополнительные параметры)
 * -- тип протокола (http/https/ftp/iptv/...)
 * -- и какие-то служебные значения для выполнения их сервером...
 * [ Сервлет ]
 * 'Сервлет' - это прежде всего (веб)приложение, которое работает на серверной стороне и расширяет функциональные возможности Сервера Приложений.
 * Жизненный цыкл сервлета определяется методами: init/service/destroy.
 *    Но по умолчанию в сервлете достаточно переопределить метод для нужного типа запроса...
 * [ Связь между клиентом и сервлетом ]
 * 'Сервер приложений' состоит из:
 * - 'сокета' -  который как правило уже настроен на (веб)приложение 'дефолтного сервлета'.
 * - и 'дефолтного сервлета' - который определяет к какому сервлету принадлежит клиентский запрос/обращение:
 * -- это может быть либо конкретный сервлет (задеплоенного (веб)приложения)
 * -- либо JSP-страница
 * [ ... ]
 * Для (веб)приложения нужно прописать карту, в которой регистрируется сервлет и адресс-ссылка обращения к нему.
 *    (Именно по такой карте сервер приложений будет находит сервлеты...)
 * (Итак HTTP-протокол состоит из запроса и ответа (Request/Response)):
 * 1. можно сформировать ответ внутри тела самого сервлета через объект-ответа (Response).
 * 2. а можно перенаправить ответ:
 *    -- либо на JSP-страницу (прописанную в карте сервлета)
 *    -- либо через запрос (Request) на JSP-страницу с помощью добавления аттрибутов
 * [ Клиент ]
 * Обычно (веб)клиентом выступает веб-браузер - это приложение которое умеет работать с HTTP-протоколом, формировать запросы и получать/читать/представлять ответы...
 * А также клиентами могут быть и другие объекты, которые умеют работать с (объектом)AJAX-инструментом. Например: Java(HttComponent), JavaScript(jQuery/AngulaJS)...
 *    Принцип остается тотже: нужно прописывать адресс/тип/параметры для клиентского запроса
 *    В этом случае клиентом может быть любой объект/программа, которая помимо формирования запроса (поддержка работы с HTTP-протоколом), должен уметь принимать/представлять ответ...
 *    Например:
 *       - 'Vaadin' (код/скрипт формируется на сервере, но работает на клиентской стороне) - имеет специфическое API (портлет) которое
 *         позволяет синхронизировать работу клиентского кода с серверным кодом. Портлеты позволяют полностью автоматически генерить
 *         (с помощью API) согласованные/синхронные между собой объекты и даже использова внутри AJAX...
 *       - 'GWT' код/скрипт также формируется на сервере, но работает на клиентской стороне - имеет специфическое API (RPC-сервис) но (в отличии
 *         от Vaadin) 'GWT' генерит только клиентскую часть кода/скрипта (а серверную часть кода/скрипта независимая...):
 *         -- 'GWT-Dispatch' GWT-реализация диспетчера вызовов;
 *         -- 'Google Guice' позволяет организовать управление зависимостями в серверном коде с помощью Dependency Injection-подхода;
 *         -- 'Google GIN' позволяет применять DI-подход в клиентском;
 *         Технология 'GWT' по тиилю больше похожа на JSP, но умеет автоматически генерить элементы веб-формы подобно 'Vaadin'
 *
 * [ Сессии и Фильтры и кукки ]
 * Проблема при передаче данных для клиент-серверных (веб)приложений в том что с каждым новым запросом состояние между клиентом и сервером теряется...
 *    (например: после авторизации, после того как товар-покупку положили в корзину)
 *    (или наоборот: ограничить доступ между администратором и гостями на к (веб)приложению)
 * То есть, чтобы научиться контролировать состояние доступа для клиентов к (веб)приложению существуют:
 * - 'Кукки' - позволяет хранить отправляемые клиентские данные из формы в файле и прикреплять его для автоматической передачи на сервер
 *             (для выполнения повторной автоматической авторизации/вудитенфикации) но проблема в том что такие файлы можно перехватить/украсть
 *             у клиента и пользоваться этим всегда
 * - 'Сессия' - позволяет хранить отправляемые клиентские данные из формы в файле и проверять/идентифицировать клиента уже именно на стороне
 *              сервера (для выполнения повторной автоматической авторизации/вудитенфикации) этот способ защищает данные от воровста...
 * - 'Фильтры' - (делают обратное) закрывают доступ к ресурсам на стороне сервера для конкретно-указанных клиентов
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
