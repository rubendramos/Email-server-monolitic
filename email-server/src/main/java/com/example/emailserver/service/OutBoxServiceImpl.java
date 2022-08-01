package com.example.emailserver.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.emailserver.entity.Address;
import com.example.emailserver.entity.Email;
import com.example.emailserver.entity.OutBox;
import com.example.emailserver.enums.StatusEnum;
import com.example.emailserver.repository.AddressRepository;
import com.example.emailserver.repository.MailRepository;
import com.example.emailserver.repository.OutBoxRepository;
import com.example.emailserver.service.exception.MailServiceException;
import com.example.emailserver.service.exception.NoAddressDomainException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutBoxServiceImpl implements OutBoxService {

	private final OutBoxRepository outBoxRepository;

	private final AddressRepository addressRepository;
	
	private final  MailRepository messageRepository;

//	@Override
//	public Set<Message> listEmailsFromAddresAndStatus(String stringAddress, StatusEnum status)
//			throws MailServiceException {
//		Set<Message> listMails = new HashSet<>();
//
//		Address address = addressRepository.findByAddress(stringAddress);
//
//		if (address == null) {
//			throw new NoAddressDomainException(new Object[] { stringAddress });
//		}
//
//		Set<OutBox> outBoxList = outBoxRepositoty.findByAddressAndEmailStatusValue(address, status.getStatusId());
//		outBoxList.forEach(outBox -> listMails.add(outBox.getMessage()));
//		return listMails;
//	}
//
//	@Override
//	public Set<Message> listEmailsFromAddres(String addressString) throws MailServiceException {
//		Set<Message> listMails = new HashSet<>();
//		Address address = addressRepository.findByAddress(addressString);
//
//		if (address == null) {
//			throw new NoAddressDomainException(new Object[] { addressString });
//		}
//		Set<OutBox> outBoxList = outBoxRepositoty.findByAddress(address);
//		outBoxList.forEach(outBox -> listMails.add(outBox.getMessage()));
//		return listMails;
//
//	}
	
	
	@Override
	public Set<Email> listEmailsFromAddresAndStatus(String stringAddress, StatusEnum status)
			throws MailServiceException {

		Address address = addressRepository.findByAddress(stringAddress);

		if (address == null) {
			throw new NoAddressDomainException(new Object[] { stringAddress });
		}

		Set<Email> outBoxList = outBoxRepository.findByAddressAndEmailStatusValue(address, status.getStatusId());
		
		return outBoxList;
	}

	@Override
	public Set<Email> listEmailsFromAddres(String addressString) throws MailServiceException {
		Address address = addressRepository.findByAddress(addressString);

		if (address == null) {
			throw new NoAddressDomainException(new Object[] { addressString });
		}
		Set<Email> outBoxList = outBoxRepository.findByAddress(address);		
		return outBoxList;

	}
	
	
	@Override
	public Email createOutBox(Email email) throws MailServiceException {
		Email savedEmail = null;
		Address address = addressRepository.findByAddress(email.getAddress().getAddress());

		if (address == null) {
			throw new NoAddressDomainException(new Object[] { email.getAddress().toString() });
		}

		email.getMessage().setCreateAt(new Date());
		email.getMessage().setUpdateAt(null);
		email.setAddress(address);
		email.setEmailStatusValue(StatusEnum.BORRADOR.getStatusId());
		email.setEmailStatus(StatusEnum.BORRADOR);
		

				
		savedEmail = outBoxRepository.save((OutBox)email);
		messageRepository.save(email.getMessage());
		return  savedEmail;
	}
	
	
	@Override
	public Email deleteOutBox(Long id){
		Email ematilToDeleted = null;
		ematilToDeleted = outBoxRepository.getById(id);
		if (null != ematilToDeleted && ematilToDeleted.getEmailStatusValue() != StatusEnum.ELIMINADO.getStatusId()) {
			
			ematilToDeleted.setEmailStatusValue(StatusEnum.ELIMINADO.getStatusId());
			outBoxRepository.save((OutBox)ematilToDeleted);
		} else {
			ematilToDeleted = null;
		}
		
		return ematilToDeleted;
	}
	
	
	@Override
	public Set<Email> deleteOutBox(Set<Long> mailsIds) throws MailServiceException{
		Set<Email> deletedMails = new HashSet<>();
		
		mailsIds.forEach(mailId ->{
			Email deleteMail = deleteOutBox(mailId);
			if(null != deleteMail) {
				deletedMails.add(deleteMail);
			}
		});
		
		return deletedMails;
	}

}
