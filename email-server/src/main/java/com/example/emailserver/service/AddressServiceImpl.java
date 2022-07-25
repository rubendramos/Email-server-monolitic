package com.example.emailserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.emailserver.entity.Address;
import com.example.emailserver.repository.AddressRepository;
import com.example.emailserver.repository.OutBoxRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepositoty;

	@Override
	public List<Address> listAllAddress() {
		return addressRepositoty.findAll();
	}

	@Override
	public Address getAddresFromAddresString(String addresss) {
		return addressRepositoty.findByAddress(addresss);
	}

	

	
}
