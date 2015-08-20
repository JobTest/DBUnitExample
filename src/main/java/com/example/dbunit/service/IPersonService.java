package com.example.dbunit.service;

import com.example.dbunit.domain.Address;
import com.example.dbunit.domain.Person;

import java.util.List;

public interface IPersonService {

	public void addPersonAddress(int personId, Address address);

	public List<Person> getAllPersons();

	Person getPerson(int personId);

	public void save(Person person);
}
