package com.example.dbunit;

import org.dbunit.JdbcBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Саша
 * @version 1.0
 * {@link http://george-zalizko.blogspot.com/2012/10/spring-31-jpa-dbunit-gradle-maven.html}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@ActiveProfiles("test")
abstract public class DBUnitTest extends JdbcBasedDBTestCase {

	static Logger LOG;

	public DBUnitTest() {
		LOG = LoggerFactory.getLogger(this.getClass());
	}

	@Override
	protected String getConnectionUrl() {
		return "jdbc:hsqldb:mem:testdb";
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/TestDataSet.xml"));
	}

	@Override
	protected String getDriverClass() {
		return "org.hsqldb.jdbcDriver";
	}

	@Override
	protected String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return "sa";
	}
}
