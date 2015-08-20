package com.example.dbunit.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String firstname;
	private String lastname;
	@Column(name = "father", insertable = false, updatable = false)
	private Integer fatherID;
	@Column(name = "mother", insertable = false, updatable = false)
	private Integer motherID;
	@ManyToOne
	@JoinColumn(name = "father")
	private Person father;
	@ManyToOne
	@JoinColumn(name = "mother")
	private Person mother;
	@OneToMany(mappedBy = "fatherID")
	private List<Person> fatherChildren;
	@OneToMany(mappedBy = "motherID")
	private List<Person> motherChildren;
	@OneToMany(mappedBy = "person")
	private List<Address> addresses;

	public List<Address> getAddresses() {
		return this.addresses;
	}
	public List<Person> getChildren() {
		return fatherChildren != null ? fatherChildren : motherChildren;
	}
	public Person getFather() {
		return father;
	}
	public String getFirstname() {
		return this.firstname;
	}
	public int getId() {
		return this.id;
	}
	public String getLastname() {
		return this.lastname;
	}
	public Person getMother() {
		return mother;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	public void setFather(Person father) {
		this.father = father;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public void setMother(Person mother) {
		this.mother = mother;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: " + id);
		sb.append(", FirstName: " + firstname);
		sb.append(", LastName: " + lastname);
		return sb.toString();
	}
}