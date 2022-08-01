package com.example.emailserver.service;

import java.util.Set;

import com.example.emailserver.entity.Email;
import com.example.emailserver.entity.InBox;
import com.example.emailserver.enums.StatusEnum;
import com.example.emailserver.service.exception.EmailStatusException;
import com.example.emailserver.service.exception.MailServiceException;

public interface InBoxService {

	
	/**
	 * Retrieves a list o emails by address and {@link StatusEnum}
	 * @return
	 */
	public Set<Email> listEmailsFromAddresAndStatus(String addresParam, StatusEnum status) throws MailServiceException;
	
	/**
	 * Retrieves a Set of emilas by address
	 * @return
	 */
	public Set<Email> listEmailsFromAddres(String addresParam) throws MailServiceException;
	
	
	/**
	 * Sends a mail
	 * @param mailId
	 * @return
	 * @throws MailServiceException
	 * @throws EmailStatusException
	 */
	public Email sendMail(Long mailId) throws MailServiceException,EmailStatusException;
	
	/**
	 * Delete a mail in InBox by id and addressId
	 * @param id
	 * @param address
	 * @return
	 * @throws MailServiceException
	 */
	public InBox deleteInBox(Long id,Long address) throws MailServiceException;
	

	/**
	 * Delete a set of mails by   id's and address
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	public Set<Email> deleteInBox(Set<Long> mailsIds, Long address) throws MailServiceException;
}
