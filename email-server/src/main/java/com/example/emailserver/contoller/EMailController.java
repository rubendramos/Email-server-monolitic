package com.example.emailserver.contoller;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.emailserver.dto.MailBoxDTO;
import com.example.emailserver.dto.MailDTO;
import com.example.emailserver.dto.MultipleDeleteDTO;
import com.example.emailserver.dto.MultipleEmailDTO;
import com.example.emailserver.entity.Email;
import com.example.emailserver.entity.Message;
import com.example.emailserver.enums.StatusEnum;
import com.example.emailserver.mappers.EMailMapper;
import com.example.emailserver.mappers.MessageMapper;
import com.example.emailserver.service.InBoxService;
import com.example.emailserver.service.MessageService;
import com.example.emailserver.service.OutBoxService;
import com.example.emailserver.service.exception.EmailStatusException;
import com.example.emailserver.service.exception.MailServiceException;

@RestController
@RequestMapping(value = "/emails")
public class EMailController {

	Logger logger = LoggerFactory.getLogger(EMailController.class);
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private OutBoxService outBoxService;
	
	@Autowired
	private InBoxService inBoxService;

	@GetMapping("/allMessages")
	public ResponseEntity<Set<MailDTO>> listMails() {
		Set<Message> mails = messageService.listAllEmails();
		if (!mails.isEmpty()) {
			return ResponseEntity.ok(MessageMapper.convertToDTO(mails));
		}
		return ResponseEntity.noContent().build();

	}

	@GetMapping(value = "message/{messagelId}")
	public ResponseEntity<MailDTO> mailById(@PathVariable("messagelId") Long messageId) {
		Message mail = messageService.getMailById(messageId);
		if (mail != null) {
			return ResponseEntity.ok(MessageMapper.convertToDTO(mail));
		}
		return ResponseEntity.noContent().build();

	}

	@GetMapping("/outbox")
	public ResponseEntity<Set<MailDTO>> listOutBoxMails(@RequestParam String addressParam,
			@RequestParam int statusParam) throws MailServiceException {		
		StatusEnum statusEnum = StatusEnum.of(statusParam);
		Set<Email> mails = outBoxService.listEmailsFromAddresAndStatus(addressParam, statusEnum);
		if (!mails.isEmpty()) {
			return ResponseEntity.ok(EMailMapper.convertToDTO(mails));
		}
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/inbox")
	public ResponseEntity<Set<MailDTO>> listIntBoxMails(@RequestParam String addressParam,
			@RequestParam int statusParam) throws MailServiceException{
		
		StatusEnum statusEnum = StatusEnum.of(statusParam);
		Set<Email> mails = inBoxService.listEmailsFromAddresAndStatus(addressParam, statusEnum);
		if (!mails.isEmpty()) {
			return ResponseEntity.ok(EMailMapper.convertToDTO(mails));
		}
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/mailbox")
	public ResponseEntity<Set<MailDTO>> listMailBox(@RequestBody MailBoxDTO mailBoxDTO) throws MailServiceException{
		Set<Email> mails = null;
		StatusEnum statusEnum = mailBoxDTO.getEmailStatus();
		String emailAdress = mailBoxDTO.getEmailAddress();
		
		switch(mailBoxDTO.getMailBoxType()) {
			case INBOX :
				mails = inBoxService.listEmailsFromAddresAndStatus(emailAdress, statusEnum);
				break;
			case OUTBOX:
				mails = outBoxService.listEmailsFromAddresAndStatus(emailAdress, statusEnum);
		}
		
		if (!mails.isEmpty()) {
			return ResponseEntity.ok(EMailMapper.convertToDTO(mails));
		}
		return ResponseEntity.noContent().build();
	}


//	@PostMapping
//	public ResponseEntity<MailDTO> createMail(@RequestBody MailDTO mailDTO) {
//
//		Message savedMail = null;
//
//		try {
//
//			if (mailDTO != null) {
//				Message mail = MailMapper.convertToEntity(mailDTO);
//
//				savedMail = messageService.createMail(mail);
//			}
//
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//
//		return ResponseEntity.status(HttpStatus.CREATED).body(MailMapper.convertToDTO(savedMail));
//	}
	
	@PostMapping
	public ResponseEntity<MailDTO> createMail(@RequestBody MailDTO mailDTO) {

		Email savedMail = null;

		try {

			if (mailDTO != null) {
				Email mail = EMailMapper.convertToEntity(mailDTO);
				savedMail = outBoxService.createOutBox(mail);
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(EMailMapper.convertToDTO(savedMail));
	}

//	@PostMapping("createMultipleEmails")
//	public ResponseEntity<Set<MailDTO>> createMail(@RequestBody MultipleEmailDTO multipleMailDTO) {
//
//		Set<Message> savedMailSet = null;
//
//		try {
//
//			if (multipleMailDTO != null && null != multipleMailDTO.getEmailsDTOs() && !multipleMailDTO.getEmailsDTOs().isEmpty() ) {
//				savedMailSet = saveMailSet(multipleMailDTO.getEmailsDTOs());
//			}
//
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//
//		return ResponseEntity.status(HttpStatus.CREATED).body(MailMapper.convertToDTO(savedMailSet));
//	}
	
	
	@PostMapping("createMultipleEmails")
	public ResponseEntity<Set<MailDTO>> createMail(@RequestBody MultipleEmailDTO multipleMailDTO) {

		Set<Email> savedMailSet = null;

		try {

			if (multipleMailDTO != null && null != multipleMailDTO.getEmailsDTOs() && !multipleMailDTO.getEmailsDTOs().isEmpty() ) {
				savedMailSet = saveMailSet(multipleMailDTO.getEmailsDTOs());
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(EMailMapper.convertToDTO(savedMailSet));
	}
	

	
//	private Set<Message> saveMailSet(Set<MailDTO> mailDTOSet) {
//		Set<Message> savedMailSet = new HashSet<>();
//		mailDTOSet.forEach(mailDTO -> {
//			try {
//				Message savedMail = null;
//				Message mail = MailMapper.convertToEntity(mailDTO);
//				savedMail = mailService.createMail(mail);
//				if (null != savedMail) {
//					savedMailSet.add(savedMail);
//				}
//			} catch (Exception e) {
//				logger.warn("No se ha podido guardar el mail con sender: " + mailDTO.toString());
//			}
//		});
//		
//		return savedMailSet;
//	}
	
	private Set<Email> saveMailSet(Set<MailDTO> mailDTOSet) {
		Set<Email> savedMailSet = new HashSet<>();
		mailDTOSet.forEach(mailDTO -> {
			try {
				Email savedMail = null;
				Email mail = EMailMapper.convertToEntity(mailDTO);
				savedMail = outBoxService.createOutBox(mail);
				if (null != savedMail) {
					savedMailSet.add(savedMail);
				}
			} catch (Exception e) {
				logger.warn("No se ha podido guardar el mail con sender: " + mailDTO.toString());
			}
		});
		
		return savedMailSet;
	}

//	@PutMapping(value = "/{mailId}")
//	public ResponseEntity<MailDTO> updateMail(@PathVariable("mailId") Long mailId, @RequestBody MailDTO mailDTO) {
//		Message mailUpdated = null;
//
//		try {
//
//			mailDTO.setMailId(mailId);
//			mailUpdated = messageService.updateMail(MailMapper.convertToEntity(mailDTO));
//			if (mailUpdated == null) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//			}
//
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//
//		return ResponseEntity.ok(MailMapper.convertToDTO(mailUpdated));
//	}
	
	@PutMapping(value = "/{mailId}")
	public ResponseEntity<MailDTO> updateMail(@PathVariable("mailId") Long mailId, @RequestBody MailDTO mailDTO) throws MailServiceException {
		Message messaUpdated = null;
		Email emailUpdated = null;

		try {

			mailDTO.setMailId(mailId);
			emailUpdated = EMailMapper.convertToEntity(mailDTO);
			messaUpdated = messageService.updateMail(EMailMapper.convertToEntity(mailDTO).getMessage());
			if (messaUpdated == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			emailUpdated.setMessage(messaUpdated);
			emailUpdated.setEmailStatusValue(messaUpdated.getOutBox().getEmailStatusValue());
			
		} catch (Exception e) {
			throw new MailServiceException(e, new Object [] {mailId});
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(emailUpdated));
	}	
		

	@DeleteMapping(value = "/{mailId}")
	public ResponseEntity<MailDTO> deleteMail(@PathVariable("mailId") Long mailId) {
		Message mailDeleted = null;

		try {

			mailDeleted = messageService.deleteMail(mailId);
			if (mailDeleted == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok(MessageMapper.convertToDTO(mailDeleted));
	}

	@PostMapping("/deleteMultiple")
	public ResponseEntity<Set<MailDTO>> deleteMultipleMail(@RequestBody MultipleDeleteDTO emailsIds) {
		Set<Email> deletedMails = null;

		try {
			
			switch(emailsIds.getMailBoxType()) {
			case INBOX :
				deletedMails = inBoxService.deleteInBox(emailsIds.getEmailIDs(),emailsIds.getAddressId());
				break;
			case OUTBOX:
				deletedMails = outBoxService.deleteOutBox(emailsIds.getEmailIDs());
		}
			

			if (deletedMails == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(deletedMails));
	}
	
	
	@PostMapping(value = "/sendMailById/{mailId}")
	public ResponseEntity<MailDTO> sendMailById(@PathVariable("mailId") Long mailId) throws MailServiceException, EmailStatusException {
		Email mailSended = null;

	
			mailSended = inBoxService.sendMail(mailId);
			if (mailSended == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

	
		return ResponseEntity.ok(EMailMapper.convertToDTO(mailSended));
	}
	

 
	
	
	
}
