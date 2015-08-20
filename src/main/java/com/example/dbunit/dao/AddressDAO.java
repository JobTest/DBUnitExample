package com.example.dbunit.dao;

import org.springframework.stereotype.Repository;

import com.example.dbunit.domain.Address;

@Repository
public class AddressDAO extends JpaDAO<Address> implements IAddressDAO {

	public AddressDAO() {
		setClazz(Address.class);
	}

}
