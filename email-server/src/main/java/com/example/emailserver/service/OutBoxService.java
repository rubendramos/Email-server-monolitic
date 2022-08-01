package com.example.emailserver.service;

import java.util.Set;

import com.example.emailserver.entity.Email;
import com.example.emailserver.enums.StatusEnum;
import com.example.emailserver.service.exception.MailServiceException;

public interface OutBoxService {

	
	/**
	 * Retrieves all mails list
	 * @return
	 */
//	public Set<Message> listEmailsFromAddresAndStatus(String address, StatusEnum status) throws MailServiceException;
//	
//	/**
//	 * Retrieves all mails list
//	 * @return
//	 */
//	public Set<Message> listEmailsFromAddres(String address) throws MailServiceException;
	
	
	
	/**
	 * Retrieves all mails list
	 * @return
	 */
	public Set<Email> listEmailsFromAddresAndStatus(String address, StatusEnum status) throws MailServiceException;
	
	/**
	 * Retrieves all mails list
	 * @return
	 */
	public Set<Email> listEmailsFromAddres(String address) throws MailServiceException;
	
	
	/**Sabe a outbox mail*/
	public Email createOutBox(Email email) throws MailServiceException;
	
	
	/**Delete a outbox mail*/
	public Email deleteOutBox(Long emailId) throws MailServiceException;
	

	/**
	 * Delete a set of mails by id
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	public Set<Email> deleteOutBox(Set<Long> mailsIds) throws MailServiceException;
	
	
	
	
}
