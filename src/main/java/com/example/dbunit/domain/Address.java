package com.example.dbunit.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int person;
	private String street;
	private int zipCode;
    private String city;
    private String houseNr;

	public String getCity() {
		return city;
	}
	public String getHouseNr() {
		return houseNr;
	}
	public int getPerson() {
		return person;
	}
	public String getStreet() {
		return street;
	}
	public int getZipCode() {
		return zipCode;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setHouseNr(String houseNr) {
		this.houseNr = houseNr;
	}
	public void setPerson(int person) {
		this.person = person;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}
}