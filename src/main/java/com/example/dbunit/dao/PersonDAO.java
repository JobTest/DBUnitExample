package com.example.dbunit.dao;

import org.springframework.stereotype.Repository;

import com.example.dbunit.domain.Person;

@Repository
public class PersonDAO extends JpaDAO<Person> implements IPersonDAO {

	public PersonDAO() {
		setClazz(Person.class);
	}

}
