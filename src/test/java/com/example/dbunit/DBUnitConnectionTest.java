package com.example.dbunit;

import org.dbunit.dataset.IDataSet;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Саша
 * @version 1.0
 * {@link http://george-zalizko.blogspot.com/2012/10/spring-31-jpa-dbunit-gradle-maven.html}
 */
public class DBUnitConnectionTest extends DBUnitTest {

	@Test
	public void connectionTest() throws Exception {
		IDataSet testDataSet = getDataSet();
		assertNotNull(testDataSet);

		String[] tables = getConnection().createDataSet().getTableNames();
		assertTrue("Please check database configuration: connectionUrl, username, password, schema.", tables.length > 0);

		LOG.debug("--------------------------" + tables.length + "---------------------------------------");
		LOG.debug(Arrays.toString(tables));
	}

}
