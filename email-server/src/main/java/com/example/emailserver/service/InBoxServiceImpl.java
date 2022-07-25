package com.example.emailserver.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.emailserver.entity.Address;
import com.example.emailserver.entity.InBox;
import com.example.emailserver.entity.Message;
import com.example.emailserver.entity.StatusEnum;
import com.example.emailserver.repository.AddressRepository;
import com.example.emailserver.repository.InBoxRepository;
import com.example.emailserver.service.exception.MailServiceException;
import com.example.emailserver.service.exception.NoAddressDomainException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InBoxServiceImpl implements InBoxService {

	private final InBoxRepository inBoxRepositoty;
	private final AddressRepository addressRepository;
	

	@Override
	public Set<Message> listEmailsFromAddresAndStatus(String stringAddress, StatusEnum status) throws MailServiceException{
		Set<Message> listMails = new HashSet<>();
		Address address = addressRepository.findByAddress(stringAddress);

		if (address == null) {
			throw new NoAddressDomainException(new Object[] { stringAddress });
		}

		Set<InBox> inBoxList = inBoxRepositoty.findByAddressAndEmailStatusValue(address, status.getStatusId());
		inBoxList.forEach(inBox -> listMails.add(inBox.getMessage()));
		return listMails;
	}
	
	@Override
	public Set<Message> listEmailsFromAddres(String stringAddress) throws MailServiceException{
		Set<Message> listMails = new HashSet<>();
		Address address = addressRepository.findByAddress(stringAddress);

		if (address == null) {
			throw new NoAddressDomainException(new Object[] { stringAddress });
		}
		
		Set<InBox> inBoxList = inBoxRepositoty.findByAddress(address);		
		inBoxList.forEach(inBox -> listMails.add(inBox.getMessage()));
		return listMails;
		
		
	}


}
