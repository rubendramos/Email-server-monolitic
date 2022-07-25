package com.example.emailserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailserver.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

	/**
	 * Retrieves adrress entity from string address
	 * @param addrress
	 * @return
	 */
	public Address findByAddress(String addrress);
}
