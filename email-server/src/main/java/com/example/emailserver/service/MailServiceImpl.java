package com.example.emailserver.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.emailserver.entity.Address;
import com.example.emailserver.entity.AddressTypeEnum;
import com.example.emailserver.entity.InBox;
import com.example.emailserver.entity.Mail;
import com.example.emailserver.entity.OutBox;
import com.example.emailserver.entity.StatusEnum;
import com.example.emailserver.repository.AddressRepository;
import com.example.emailserver.repository.InBoxRepository;
import com.example.emailserver.repository.MailRepository;
import com.example.emailserver.repository.OutBoxRepository;
import com.example.emailserver.service.exception.EmailStatusException;
import com.example.emailserver.service.exception.MailServiceException;
import com.example.emailserver.service.exception.NoAddressDomainException;
import com.example.emailserver.utils.EmailServerUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
	
	Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	private final MailRepository mailRepository;
	private final AddressRepository addressRepository;
	private final OutBoxRepository outBoxRepository;
	private final InBoxRepository inBoxRepository;

	@Override
	public Set<Mail> listAllEmails() {
		return new HashSet<>(mailRepository.findAll());
	}

	@Override
	public Mail getMailById(Long id) {
		return mailRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Mail createMail(Mail mail) throws MailServiceException {

		Address address = addressRepository.findByAddress(mail.getEmailFrom());

		if (address == null) {
			throw new NoAddressDomainException(new Object[] { mail.getEmailFrom() });
		}

		mail.setCreateAt(new Date());
		mail.addSenderToOutBox(address, StatusEnum.BORRADOR);
		outBoxRepository.save(mail.getOutBox());
		return mailRepository.save(mail);
	}

	@Override
	@Transactional
	public Mail updateMail(Mail mail) throws MailServiceException {
		Mail dbMail = this.getMailById(mail.getId());

		if (null != dbMail) {
			if (dbMail.getOutBox().getEmailStatus() != StatusEnum.BORRADOR) {
				throw new EmailStatusException("", new Object[] { mail });
			} else {

				dbMail.setEmailBody(mail.getEmailBody());
				dbMail.setEmailCc(mail.getEmailCc());
				dbMail.setEmailTo(mail.getEmailTo());
				dbMail.setRecipients(mail.getRecipients());
//				dbMail.setOutBox(mail.getOutBox());
				dbMail.setUpdateAt(new Date());
				
//				outBoxRepository.save(dbMail.getOutBox());
				return mailRepository.save(dbMail);
			}
		}
		return dbMail;
	}

	@Override
	public Mail deleteMail(Long id) {
		OutBox outBox = null;
		Mail dbMail = this.getMailById(id);
		if (null != dbMail && dbMail.getOutBox().getEmailStatus() != StatusEnum.ELIMINADO) {
			outBox = dbMail.getOutBox();
			outBox.setEmailStatus(StatusEnum.ELIMINADO);
			outBox.setEmailStatusValue(StatusEnum.ELIMINADO.getStatusId());
			outBoxRepository.save(outBox);
		} else {
			dbMail = null;
		}
		
		return dbMail;
	}
	
	
	@Override
	public Set<Mail> deleteMail(Set<Long> mailsIds) {
		Set<Mail> deletedMails = new HashSet<>();
		
		
		mailsIds.forEach(mailId ->{
			Mail deleteMail = deleteMail(mailId);
			if(null != deleteMail) {
				deletedMails.add(deleteMail);
			}
		});
		
		return deletedMails;
	}
	

	@Override
	public List<Mail> getMailByFrom(String from) {
		return mailRepository.findByEmailFrom(from);
	}

	@Override
	public Mail updateStatus(Long id, StatusEnum status) {
		Mail dbMail = this.getMailById(id);
		if (null != dbMail) {
//			dbMail.getOutBox().setEmailStatus(status);
			return mailRepository.save(dbMail);
		}
		return dbMail;
	}



	@Override
	public Mail sendMail(Long mailId) throws MailServiceException,EmailStatusException{
		Mail sendedMail = null;
		
		
		try {
		
		sendedMail = this.getMailById(mailId);
		
		Set<String> addressStringToSet = EmailServerUtils.getAddressSet(sendedMail.getEmailTo());
		Set<String> addressStringCcSet =  EmailServerUtils.getAddressSet(sendedMail.getEmailCc());
		
		Set<Address> validAddressTo = validateAddressSet(addressStringToSet);
		Set<Address> validAddressCc = validateAddressSet(addressStringCcSet);
		
		Set<InBox> inBoxTo = generateInboxFromAddress(sendedMail,validAddressTo,AddressTypeEnum.TO,StatusEnum.ENVIADO);
		Set<InBox> inBoxCC = generateInboxFromAddress(sendedMail,validAddressCc,AddressTypeEnum.CC,StatusEnum.ENVIADO);
		
		sendedMail.getRecipients().addAll(inBoxTo);
		sendedMail.getRecipients().addAll(inBoxCC);
		
		OutBox outBox = sendedMail.getOutBox();
		
		
		if(outBox.getEmailStatus() == StatusEnum.BORRADOR) {
			outBox.setEmailStatusValue(StatusEnum.ENVIADO.getStatusId());
			outBoxRepository.save(outBox);
			inBoxRepository.saveAll(sendedMail.getRecipients());
			
		} else {
			throw new EmailStatusException("Mail Status : "+ outBox.getEmailStatus()+". Mail will not be sended", new Object[] {sendedMail});
		}
		
		
		}catch(Exception e) {
			logger.error("Error saving mail with id:" + mailId);
			throw new MailServiceException(e,new Object[] {"Error sending mail with id: " +mailId ,sendedMail});
		}
		
		return sendedMail;
		
		
	}
	
	
	

	
	
	private Set<Address> validateAddressSet(Set<String> stringAddressSet){
		Set<Address> validAddress = new HashSet<>();
		stringAddressSet.forEach(stringAddress->{
			Address address = validateAddress(stringAddress);
			if(null != address) {
				validAddress.add(address);
			}
			
		});
		
		return validAddress;
	}
	
	private Address validateAddress(String address) {
		return addressRepository.findByAddress(address);
	}
	
	
	private  Set<InBox> generateInboxFromAddress(Mail mail, Set<Address> addressSet, AddressTypeEnum addressTypeEnum, StatusEnum status ){
		Set<InBox> recipients = new HashSet<>();
			if(addressSet != null && !addressSet.isEmpty()) {
			addressSet.forEach(address-> {				
				recipients.add(new InBox(mail, address, addressTypeEnum, status));
			});
		}
		return recipients;	
	}
	
	
	

}
