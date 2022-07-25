package com.example.emailserver.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailserver.entity.Address;
import com.example.emailserver.entity.OutBox;

public interface OutBoxRepository extends JpaRepository<OutBox, Long>{

	public Set<OutBox> findByAddress(Address emailAddrress);
	
	public Set<OutBox> findByAddressAndEmailStatusValue(Address emailAddrress, int emailStatusValue);
	
	
}
