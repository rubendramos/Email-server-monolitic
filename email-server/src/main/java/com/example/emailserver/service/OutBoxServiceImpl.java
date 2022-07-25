package com.example.emailserver.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.emailserver.entity.Address;
import com.example.emailserver.entity.Mail;
import com.example.emailserver.entity.OutBox;
import com.example.emailserver.entity.StatusEnum;
import com.example.emailserver.repository.AddressRepository;
import com.example.emailserver.repository.OutBoxRepository;
import com.example.emailserver.service.exception.MailServiceException;
import com.example.emailserver.service.exception.NoAddressDomainException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutBoxServiceImpl implements OutBoxService {

	private final OutBoxRepository outBoxRepositoty;

	private final AddressRepository addressRepository;

	@Override
	public Set<Mail> listEmailsFromAddresAndStatus(String stringAddress, StatusEnum status)
			throws MailServiceException {
		Set<Mail> listMails = new HashSet<>();

		Address address = addressRepository.findByAddress(stringAddress);

		if (address == null) {
			throw new NoAddressDomainException(new Object[] { stringAddress });
		}

		Set<OutBox> outBoxList = outBoxRepositoty.findByAddressAndEmailStatusValue(address, status.getStatusId());
		outBoxList.forEach(outBox -> listMails.add(outBox.getMail()));
		return listMails;
	}

	@Override
	public Set<Mail> listEmailsFromAddres(String addressString) throws MailServiceException {
		Set<Mail> listMails = new HashSet<>();
		Address address = addressRepository.findByAddress(addressString);

		if (address == null) {
			throw new NoAddressDomainException(new Object[] { addressString });
		}
		Set<OutBox> outBoxList = outBoxRepositoty.findByAddress(address);
		outBoxList.forEach(outBox -> listMails.add(outBox.getMail()));
		return listMails;

	}

}
