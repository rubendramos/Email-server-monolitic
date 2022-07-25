package com.example.emailserver.service;

import java.util.List;

import com.example.emailserver.entity.Address;

public interface AddressService {

	
	/**
	 * Retrieves all mails list
	 * @return
	 */
	public List<Address> listAllAddress();
	
	/**
	 * Retrieves all mails list
	 * @return
	 */
	public Address getAddresFromAddresString(String addresss);
	
	
	
}
