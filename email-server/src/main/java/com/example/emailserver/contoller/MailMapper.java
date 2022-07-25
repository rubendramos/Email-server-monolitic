package com.example.emailserver.contoller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.example.emailserver.entity.Address;
import com.example.emailserver.entity.AddressTypeEnum;
import com.example.emailserver.entity.InBox;
import com.example.emailserver.entity.Mail;
import com.example.emailserver.entity.MailDTO;
import com.example.emailserver.entity.OutBox;
import com.example.emailserver.entity.StatusEnum;

public class MailMapper {

	private static final String MAIL_ADDRESS_SEPARATOR=","; 
	
	public static MailDTO convertToDTO(Mail mail) {
		return parseMailToMailDTO(mail);
	}
	
	
	public static Set<MailDTO> convertToDTO(Set<Mail> mailList) {
		return parseMailToMailDTOList(mailList);
	}
	
	public static Mail convertToEntity(MailDTO mailDTO) {
		
		Mail mail = Mail.builder().createAt(mailDTO.getCreateAt())				
				.emailBody(mailDTO.getEmailBody())
				.emailFrom(mailDTO.getEmailFrom())
				.emailTo(String.join(MAIL_ADDRESS_SEPARATOR,mailDTO.getEmailTo()))
				.emailCc(String.join(MAIL_ADDRESS_SEPARATOR,mailDTO.getEmailCc()))
				.updateAt(mailDTO.getUpdateAt())
				.id(mailDTO.getMailId())
				.build();

		mail.setOutBox(addSenderFromMailDTO(mail, mailDTO));
//		mail.setRecipients(addRecipientsFromMailDTO(mail, mailDTO));
		
		return mail;
	}
	
	
	private static MailDTO parseMailToMailDTO(Mail mail) {
		MailDTO mailDTO = MailDTO.builder().mailId(mail.getId())
				.emailBody(mail.getEmailBody())				
				.createAt(mail.getCreateAt())
				.updateAt(mail.getUpdateAt())
				.emailFrom(mail.getOutBox().getEmailAddress().getAddress())
				.emailTo(new HashSet<String>(Arrays.asList(mail.getEmailTo().split(MAIL_ADDRESS_SEPARATOR))))
				.emailCc(new HashSet<String>(Arrays.asList(mail.getEmailCc().split(MAIL_ADDRESS_SEPARATOR))))
				.emailStatus(mail.getOutBox().getEmailStatus()).build();
//		addEmailAddressFromRecipients(mailDTO, mail.getRecipients());
		return mailDTO;
	}
	
	private static Set<MailDTO> parseMailToMailDTOList(Set<Mail> mails) {
		Set<MailDTO> mailDTOList = new HashSet<>();
		mails.forEach(mail -> mailDTOList.add(parseMailToMailDTO(mail)));
		return mailDTOList;
	}
	
	/**
	 * Add an address to TO,CC or BCC List depends on AddressTypeEnum
	 * 
	 * @param address
	 */
	private static void addEmailAddressFromRecipients(MailDTO mailDTO, Set<InBox> emailAddressSet) {

		emailAddressSet.forEach(emailAddress -> {
			switch (emailAddress.getEmailAddressType()) {
			case TO:
				mailDTO.getEmailTo().add(emailAddress.getAddress().getAddress());
				break;
			case CC:
				mailDTO.getEmailCc().add(emailAddress.getAddress().getAddress());
				break;
			case BCC:
				break;
			}
		});
	}
	
	
	private static OutBox addSenderFromMailDTO(Mail mail, MailDTO mailDTO){
		Address senderAddress = Address.builder().address(mailDTO.getEmailFrom()).build();
		return new OutBox(mail, senderAddress, mailDTO.getEmailStatus());
	}
	
	private static Set<InBox> addRecipientsFromMailDTO(Mail mail, MailDTO mailDTO){
		Set<InBox> recipients = new HashSet<>();
		recipients.addAll(getEmailAddress(mail,mailDTO.getEmailTo(), AddressTypeEnum.TO, mail.getOutBox().getEmailStatus()));
		recipients.addAll(getEmailAddress(mail,mailDTO.getEmailCc(), AddressTypeEnum.BCC, mail.getOutBox().getEmailStatus()));
		return recipients;
	}
	
	private static Set<InBox> getEmailAddress(Mail mail, Set<String> addressList, AddressTypeEnum addressTypeEnum, StatusEnum status ){
		Set<InBox> recipients = new HashSet<>();
			if(addressList != null && !addressList.isEmpty()) {
			addressList.forEach(addressString-> {
				Address address = Address.builder().address(addressString).build();
				recipients.add(new InBox(mail, address, addressTypeEnum, status));
			});
		}
		return recipients;	
	}
	
}