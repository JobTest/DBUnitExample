package com.example.dbunit;

import com.example.dbunit.domain.Person;
import com.example.dbunit.service.IPersonService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.PersistenceException;

import static org.junit.Assert.assertEquals;

/**
 * @author Саша
 * @version 1.0
 * {@link http://george-zalizko.blogspot.com/2012/10/spring-31-jpa-dbunit-gradle-maven.html}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@ActiveProfiles("test")
public class SpringTest extends AbstractTransactionalJUnit4SpringContextTests {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(SpringTest.class);

	@Autowired
    IPersonService personService;

	@Test
	public void addPersonTest() {
		Person person = new Person();
		person.setFirstname("SOMENAME");

		personService.save(person);

		Person person2 = new Person();
		person2.setFirstname("SOMENAME2");

		personService.save(person2);

		Assert.assertEquals(2, countRowsInTable(Person.class.getSimpleName()));
	}

	// impossible to insert two persons with the same id
	@Test(expected = PersistenceException.class)
	public void addPersonThrowsExceptionTest() {
		Person person = new Person();
		person.setId(1);
		person.setFirstname("SOMENAME");

		personService.save(person);

		Person person2 = new Person();
		person2.setId(1);
		person2.setFirstname("SOMENAME");

		personService.save(person2);
	}

	@Test
	public void addPersonWithFather() {
		Person son = new Person();
		son.setFirstname("Andrew");
		son.setLastname("Smith");
		personService.save(son);

		Person father = new Person();
		father.setFirstname("Andrew father");
		personService.save(father);

		son.setFather(father);
		personService.save(son);

		assertEquals(2, countRowsInTable(Person.class.getSimpleName()));
	}
}
