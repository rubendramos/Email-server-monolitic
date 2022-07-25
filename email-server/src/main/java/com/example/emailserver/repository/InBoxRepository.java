package com.example.emailserver.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailserver.entity.Address;
import com.example.emailserver.entity.InBox;
import com.example.emailserver.entity.OutBox;

public interface InBoxRepository extends JpaRepository<InBox, Long>{

	public Set<InBox> findByAddress(Address emailAddrress);
	
	public Set<InBox> findByAddressAndEmailStatusValue(Address emailAddrress, int emailStatusValue);
	
	
}
