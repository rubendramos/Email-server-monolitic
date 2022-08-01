package com.example.emailserver.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailserver.entity.Address;
import com.example.emailserver.entity.Email;
import com.example.emailserver.entity.EmailAddressKey;
import com.example.emailserver.entity.InBox;

public interface InBoxRepository extends JpaRepository<InBox, Long>{

	public Set<Email> findByAddress(Address emailAddrress);
	
	public Set<Email> findByAddressAndEmailStatusValue(Address emailAddrress, int emailStatusValue);
	
	public InBox findById(EmailAddressKey emailAddressKey);
	
	
}
