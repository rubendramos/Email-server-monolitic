package com.example.emailserver.contoller;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder.Case;

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

import com.example.emailserver.entity.Address;
import com.example.emailserver.entity.Message;
import com.example.emailserver.entity.MailBoxDTO;
import com.example.emailserver.entity.MailBoxType;
import com.example.emailserver.entity.MailDTO;
import com.example.emailserver.entity.MultipleEmailDTO;
import com.example.emailserver.entity.MultipleIdDTO;
import com.example.emailserver.entity.StatusEnum;
import com.example.emailserver.service.InBoxService;
import com.example.emailserver.service.MailService;
import com.example.emailserver.service.OutBoxService;
import com.example.emailserver.service.exception.EmailStatusException;
import com.example.emailserver.service.exception.MailServiceException;

@RestController
@RequestMapping(value = "/emails")
public class MailController {

	Logger logger = LoggerFactory.getLogger(MailController.class);
	
	@Autowired
	private MailService mailService;

	@Autowired
	private OutBoxService outBoxService;
	
	@Autowired
	private InBoxService inBoxService;

	@GetMapping("/allEmails")
	public ResponseEntity<Set<MailDTO>> listMails() {
		Set<Message> mails = mailService.listAllEmails();
		if (!mails.isEmpty()) {
			return ResponseEntity.ok(MailMapper.convertToDTO(mails));
		}
		return ResponseEntity.noContent().build();

	}

	@GetMapping(value = "/{mailId}")
	public ResponseEntity<MailDTO> mailById(@PathVariable("mailId") Long mailId) {
		Message mail = mailService.getMailById(mailId);
		if (mail != null) {
			return ResponseEntity.ok(MailMapper.convertToDTO(mail));
		}
		return ResponseEntity.noContent().build();

	}

	@GetMapping("/outbox")
	public ResponseEntity<Set<MailDTO>> listOutBoxMails(@RequestParam String addressParam,
			@RequestParam int statusParam) throws MailServiceException {		
		StatusEnum statusEnum = StatusEnum.of(statusParam);
		Set<Message> mails = outBoxService.listEmailsFromAddresAndStatus(addressParam, statusEnum);
		if (!mails.isEmpty()) {
			return ResponseEntity.ok(MailMapper.convertToDTO(mails));
		}
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/inbox")
	public ResponseEntity<Set<MailDTO>> listIntBoxMails(@RequestParam String addressParam,
			@RequestParam int statusParam) throws MailServiceException{
		
		StatusEnum statusEnum = StatusEnum.of(statusParam);
		Set<Message> mails = inBoxService.listEmailsFromAddresAndStatus(addressParam, statusEnum);
		if (!mails.isEmpty()) {
			return ResponseEntity.ok(MailMapper.convertToDTO(mails));
		}
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/mailbox")
	public ResponseEntity<Set<MailDTO>> listMailBox(@RequestBody MailBoxDTO mailBoxDTO) throws MailServiceException{
		Set<Message> mails = null;
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
			return ResponseEntity.ok(MailMapper.convertToDTO(mails));
		}
		return ResponseEntity.noContent().build();
	}


	@PostMapping
	public ResponseEntity<MailDTO> createMail(@RequestBody MailDTO mailDTO) {

		Message savedMail = null;

		try {

			if (mailDTO != null) {
				Message mail = MailMapper.convertToEntity(mailDTO);

				savedMail = mailService.createMail(mail);
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(MailMapper.convertToDTO(savedMail));
	}

	@PostMapping("createMultipleEmails")
	public ResponseEntity<Set<MailDTO>> createMail(@RequestBody MultipleEmailDTO multipleMailDTO) {

		Set<Message> savedMailSet = null;

		try {

			if (multipleMailDTO != null && null != multipleMailDTO.getEmailsDTOs() && !multipleMailDTO.getEmailsDTOs().isEmpty() ) {
				savedMailSet = saveMailSet(multipleMailDTO.getEmailsDTOs());
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(MailMapper.convertToDTO(savedMailSet));
	}

	
	private Set<Message> saveMailSet(Set<MailDTO> mailDTOSet) {
		Set<Message> savedMailSet = new HashSet<>();
		mailDTOSet.forEach(mailDTO -> {
			try {
				Message savedMail = null;
				Message mail = MailMapper.convertToEntity(mailDTO);
				savedMail = mailService.createMail(mail);
				if (null != savedMail) {
					savedMailSet.add(savedMail);
				}
			} catch (Exception e) {
				logger.warn("No se ha podido guardar el mail con sender: " + mailDTO.toString());
			}
		});
		
		return savedMailSet;
	}

	@PutMapping(value = "/{mailId}")
	public ResponseEntity<MailDTO> updateMail(@PathVariable("mailId") Long mailId, @RequestBody MailDTO mailDTO) {
		Message mailUpdated = null;

		try {

			mailDTO.setMailId(mailId);
			mailUpdated = mailService.updateMail(MailMapper.convertToEntity(mailDTO));
			if (mailUpdated == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok(MailMapper.convertToDTO(mailUpdated));
	}
		

	@DeleteMapping(value = "/{mailId}")
	public ResponseEntity<MailDTO> deleteMail(@PathVariable("mailId") Long mailId) {
		Message mailDeleted = null;

		try {

			mailDeleted = mailService.deleteMail(mailId);
			if (mailDeleted == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok(MailMapper.convertToDTO(mailDeleted));
	}

	@PostMapping("/deleteMultiple")
	public ResponseEntity<Set<MailDTO>> deleteMultipleMail(@RequestBody MultipleIdDTO emailsIds) {
		Set<Message> deletedMails = null;

		try {

			deletedMails = mailService.deleteMail(emailsIds.getEmailIDs());
			if (deletedMails == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok(MailMapper.convertToDTO(deletedMails));
	}
	
	
	@PostMapping(value = "/sendMailById/{mailId}")
	public ResponseEntity<MailDTO> sendMailById(@PathVariable("mailId") Long mailId) throws MailServiceException, EmailStatusException {
		Message mailSended = null;

	
			mailSended = mailService.sendMail(mailId);
			if (mailSended == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

	
		return ResponseEntity.ok(MailMapper.convertToDTO(mailSended));
	}
	

 
	
	
	
}
