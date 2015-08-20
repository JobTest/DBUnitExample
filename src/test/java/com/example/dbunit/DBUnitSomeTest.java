package com.example.dbunit;

import com.example.dbunit.domain.Person;
import com.example.dbunit.service.IPersonService;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

/**
 * @author Саша
 * @version 1.0
 * {@link http://george-zalizko.blogspot.com/2012/10/spring-31-jpa-dbunit-gradle-maven.html}
 */
public class DBUnitSomeTest extends DBUnitTest {

	@Autowired
    IPersonService personService;

	@After
	public void after() throws DatabaseUnitException, SQLException, Exception {
		DatabaseOperation.DELETE_ALL.execute(getConnection(), getDataSet());
	}

	@Before
	public void before() throws DatabaseUnitException, SQLException, Exception {
		DatabaseOperation.DELETE_ALL.execute(getConnection(), getDataSet());
	}

	@Test
	public void someTest() throws DataSetException, SQLException, Exception {
		Person person = new Person();
		person.setFirstname("Georgii");
		personService.save(person);

		IDataSet actualDataSet = getConnection().createDataSet();

 		/* Export dataset into the file */
//		FlatXmlWriter writer2 = new FlatXmlWriter(new FileOutputStream("actualDataSet2.xml"));
//		writer2.setIncludeEmptyTable(false);
//		writer2.write(actualDataSet);
//		FlatXmlDataSet expectedDataSet2 = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/expectedDataSet2.xml"));

		XmlDataSet expectedDataSet = new XmlDataSet(getClass().getResourceAsStream("/expectedDataSet.xml"));
		Assertion.assertEquals(expectedDataSet, actualDataSet);
	}
}
