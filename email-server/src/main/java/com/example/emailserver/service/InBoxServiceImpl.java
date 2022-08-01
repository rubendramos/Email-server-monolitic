package com.example.emailserver.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.emailserver.entity.Address;
import com.example.emailserver.entity.Email;
import com.example.emailserver.entity.EmailAddressKey;
import com.example.emailserver.entity.InBox;
import com.example.emailserver.entity.Message;
import com.example.emailserver.entity.OutBox;
import com.example.emailserver.enums.AddressTypeEnum;
import com.example.emailserver.enums.StatusEnum;
import com.example.emailserver.repository.AddressRepository;
import com.example.emailserver.repository.InBoxRepository;
import com.example.emailserver.repository.OutBoxRepository;
import com.example.emailserver.service.exception.EmailStatusException;
import com.example.emailserver.service.exception.MailServiceException;
import com.example.emailserver.service.exception.NoAddressDomainException;
import com.example.emailserver.utils.EmailServerUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InBoxServiceImpl implements InBoxService {

	Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

	private final InBoxRepository inBoxRepository;
	private final OutBoxRepository outBoxRepository;
	private final AddressRepository addressRepository;


	@Override	
	public Set<Email> listEmailsFromAddresAndStatus(String stringAddress, StatusEnum status)
			throws MailServiceException {
		Address address = addressRepository.findByAddress(stringAddress);

		if (address == null) {
			throw new NoAddressDomainException(new Object[] { stringAddress });
		}

		Set<Email> inBoxList = inBoxRepository.findByAddressAndEmailStatusValue(address, status.getStatusId());
		return inBoxList;
	}

	@Override
	public Set<Email> listEmailsFromAddres(String stringAddress) throws MailServiceException {
		Address address = addressRepository.findByAddress(stringAddress);

		if (address == null) {
			throw new NoAddressDomainException(new Object[] { stringAddress });
		}

		Set<Email> inBoxList = inBoxRepository.findByAddress(address);
		return inBoxList;

	}

	@Override
	public Email sendMail(Long mailId) throws MailServiceException, EmailStatusException {
		Optional<OutBox> optinal = null;
		optinal = outBoxRepository.findById(mailId);
		OutBox outBoxMail = optinal.get();

		try {

			Set<String> addressStringToSet = EmailServerUtils.getAddressSet(outBoxMail.getMessage().getEmailTo());
			Set<String> addressStringCcSet = EmailServerUtils.getAddressSet(outBoxMail.getMessage().getEmailCc());

			Set<Address> validAddressTo = validateAddressSet(addressStringToSet);
			Set<Address> validAddressCc = validateAddressSet(addressStringCcSet);

			Set<InBox> inBoxTo = generateInboxFromAddress(outBoxMail.getMessage(), validAddressTo, AddressTypeEnum.TO,
					StatusEnum.ENVIADO);
			Set<InBox> inBoxCC = generateInboxFromAddress(outBoxMail.getMessage(), validAddressCc, AddressTypeEnum.CC,
					StatusEnum.ENVIADO);

			outBoxMail.getMessage().getRecipients().addAll(inBoxTo);
			outBoxMail.getMessage().getRecipients().addAll(inBoxCC);

			if (outBoxMail.getEmailStatus() == StatusEnum.BORRADOR) {
				outBoxMail.setEmailStatusValue(StatusEnum.ENVIADO.getStatusId());
				inBoxRepository.saveAll(outBoxMail.getMessage().getRecipients());
				outBoxRepository.save(outBoxMail);

			} else {
				throw new EmailStatusException(
						"Mail Status : " + outBoxMail.getEmailStatus() + ". Mail will not be sended",
						new Object[] { outBoxMail });
			}

		} catch (Exception e) {
			logger.error("Error saving mail with id:" + mailId);
			throw new MailServiceException(e, new Object[] { "Error sending mail with id: " + mailId, outBoxMail });
		}

		return outBoxMail;

	}


	@Override
	public InBox deleteInBox(Long id, Long address) {
		InBox ematilToDeleted = null;
		EmailAddressKey eak = new EmailAddressKey();
		eak.setMessageId(id);
		eak.setAddressId(address);
		ematilToDeleted = inBoxRepository.findById(eak);
		if (null != ematilToDeleted && ematilToDeleted.getEmailStatusValue() != StatusEnum.ELIMINADO.getStatusId()) {

			ematilToDeleted.setEmailStatusValue(StatusEnum.ELIMINADO.getStatusId());
			inBoxRepository.save(ematilToDeleted);
		} else {
			ematilToDeleted = null;
		}

		return ematilToDeleted;
	}

	@Override
	public Set<Email> deleteInBox(Set<Long> mailsIds, Long address) {
		Set<Email> deletedMails = new HashSet<>();

		mailsIds.forEach(mailId -> {
			Email deleteMail = deleteInBox(mailId, address);
			if (null != deleteMail) {
				deletedMails.add(deleteMail);
			}
		});

		return deletedMails;
	}

	
	private Set<Address> validateAddressSet(Set<String> stringAddressSet) {
		Set<Address> validAddress = new HashSet<>();
		stringAddressSet.forEach(stringAddress -> {
			Address address = validateAddress(stringAddress);
			if (null != address) {
				validAddress.add(address);
			}

		});

		return validAddress;
	}

	private Address validateAddress(String address) {
		return addressRepository.findByAddress(address);
	}

	private Set<InBox> generateInboxFromAddress(Message mail, Set<Address> addressSet, AddressTypeEnum addressTypeEnum,
			StatusEnum status) {
		Set<InBox> recipients = new HashSet<>();
		if (addressSet != null && !addressSet.isEmpty()) {
			addressSet.forEach(address -> {
				recipients.add(new InBox(mail, address, addressTypeEnum, status));
			});
		}
		return recipients;
	}

	
}
