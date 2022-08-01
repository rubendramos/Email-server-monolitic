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
import com.example.emailserver.entity.Message;
import com.example.emailserver.entity.OutBox;
import com.example.emailserver.enums.StatusEnum;
import com.example.emailserver.repository.AddressRepository;
import com.example.emailserver.repository.InBoxRepository;
import com.example.emailserver.repository.MailRepository;
import com.example.emailserver.repository.OutBoxRepository;
import com.example.emailserver.service.exception.EmailStatusException;
import com.example.emailserver.service.exception.MailServiceException;
import com.example.emailserver.service.exception.NoAddressDomainException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
	
	private static final String ERROR_UPDATE_MESSAGE = "No se puede actualizar el mensage. Su estado es de ";
	
	Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
	

	private final MailRepository mailRepository;
	private final AddressRepository addressRepository;
	private final OutBoxRepository outBoxRepository;


	@Override
	public Set<Message> listAllEmails() {
		return new HashSet<>(mailRepository.findAll());
	}

	@Override
	public Message getMailById(Long id) {
		return mailRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Message createMail(Message mail) throws MailServiceException {

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
	public Message updateMail(Message mail) throws MailServiceException {
		Message dbMail = this.getMailById(mail.getId());

		if (null != dbMail) {
			if (dbMail.getOutBox().getEmailStatus() != StatusEnum.BORRADOR) {
				throw new EmailStatusException(ERROR_UPDATE_MESSAGE + dbMail.getOutBox().getEmailStatus(), new Object[] { mail });
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
	public Message deleteMail(Long id) {
		OutBox outBox = null;
		Message dbMail = this.getMailById(id);
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
	public Set<Message> deleteMail(Set<Long> mailsIds) {
		Set<Message> deletedMails = new HashSet<>();
		
		
		mailsIds.forEach(mailId ->{
			Message deleteMail = deleteMail(mailId);
			if(null != deleteMail) {
				deletedMails.add(deleteMail);
			}
		});
		
		return deletedMails;
	}
	

	@Override
	public List<Message> getMailByFrom(String from) {
		return mailRepository.findByEmailFrom(from);
	}

	@Override
	public Message updateStatus(Long id, StatusEnum status) {
		Message dbMail = this.getMailById(id);
		if (null != dbMail) {
//			dbMail.getOutBox().setEmailStatus(status);
			return mailRepository.save(dbMail);
		}
		return dbMail;
	}


//
//	@Override
//	public Message sendMail(Long mailId) throws MailServiceException,EmailStatusException{
//		Message sendedMail = null;
//		
//		
//		try {
//		
//		sendedMail = this.getMailById(mailId);
//		
//		Set<String> addressStringToSet = EmailServerUtils.getAddressSet(sendedMail.getEmailTo());
//		Set<String> addressStringCcSet =  EmailServerUtils.getAddressSet(sendedMail.getEmailCc());
//		
//		Set<Address> validAddressTo = validateAddressSet(addressStringToSet);
//		Set<Address> validAddressCc = validateAddressSet(addressStringCcSet);
//		
//		Set<InBox> inBoxTo = generateInboxFromAddress(sendedMail,validAddressTo,AddressTypeEnum.TO,StatusEnum.ENVIADO);
//		Set<InBox> inBoxCC = generateInboxFromAddress(sendedMail,validAddressCc,AddressTypeEnum.CC,StatusEnum.ENVIADO);
//		
//		sendedMail.getRecipients().addAll(inBoxTo);
//		sendedMail.getRecipients().addAll(inBoxCC);
//		
//		OutBox outBox = sendedMail.getOutBox();
//		
//		
//		if(outBox.getEmailStatus() == StatusEnum.BORRADOR) {
//			outBox.setEmailStatusValue(StatusEnum.ENVIADO.getStatusId());
//			inBoxRepository.saveAll(sendedMail.getRecipients());
//			outBoxRepository.save(outBox);
//			
//			
//		} else {
//			throw new EmailStatusException("Mail Status : "+ outBox.getEmailStatus()+". Mail will not be sended", new Object[] {sendedMail});
//		}
//		
//		
//		}catch(Exception e) {
//			logger.error("Error saving mail with id:" + mailId);
//			throw new MailServiceException(e,new Object[] {"Error sending mail with id: " +mailId ,sendedMail});
//		}
//		
//		return sendedMail;
//		
//		
//	}
//	
	
	

	
	
//	private Set<Address> validateAddressSet(Set<String> stringAddressSet){
//		Set<Address> validAddress = new HashSet<>();
//		stringAddressSet.forEach(stringAddress->{
//			Address address = validateAddress(stringAddress);
//			if(null != address) {
//				validAddress.add(address);
//			}
//			
//		});
//		
//		return validAddress;
//	}
//	
//	private Address validateAddress(String address) {
//		return addressRepository.findByAddress(address);
//	}
//	
//	
//	private  Set<InBox> generateInboxFromAddress(Message mail, Set<Address> addressSet, AddressTypeEnum addressTypeEnum, StatusEnum status ){
//		Set<InBox> recipients = new HashSet<>();
//			if(addressSet != null && !addressSet.isEmpty()) {
//			addressSet.forEach(address-> {				
//				recipients.add(new InBox(mail, address, addressTypeEnum, status));
//			});
//		}
//		return recipients;	
//	}
	
	
	

}
