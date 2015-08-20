package com.example.dbunit.dao;

import com.example.dbunit.domain.Address;
import org.springframework.stereotype.Repository;

@Repository
//public interface IAddressDAORepository extends JpaRepository<Address, Integer>
public interface IAddressDAO extends IJpaDAO<Address> {

}
