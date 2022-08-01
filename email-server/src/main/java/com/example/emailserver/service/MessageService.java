package com.example.emailserver.service;

import java.util.List;
import java.util.Set;

import com.example.emailserver.entity.Message;
import com.example.emailserver.enums.StatusEnum;
import com.example.emailserver.service.exception.MailServiceException;

public interface MessageService {

	/**
	 * Retrieves all mails list
	 * @return
	 */
	public Set<Message> listAllEmails();
	
	/**
	 * Gets mail by id
	 * @param id
	 * @return
	 */
	public Message getMailById(Long id);
	
	/**
	 * Create a mail
	 * @param mail
	 * @return
	 */
	public Message createMail(Message mail) throws MailServiceException;	
	/**
	 * Update a mail
	 * @param mail
	 * @return
	 */
	public Message updateMail(Message mail) throws MailServiceException;
	
	/**
	 * Delete a mail
	 * @param id
	 * @return
	 */
	public Message deleteMail(Long id);
	
	
	/**
	 * Delete multiple mails
	 * @param id
	 * @return
	 */
	public Set<Message> deleteMail(Set<Long> id);
	
	/**
	 * Gets mails by emailfrom
	 * @param from
	 * @return
	 */
	public List<Message> getMailByFrom(String emailAddrress);
	
	/**
	 * Update mail status
	 * @param id
	 * @param status
	 * @return
	 */
	public Message updateStatus(Long id, StatusEnum status);
	
//	/**
//	 * Send a mail
//	 * @param idMail
//	 * @return
//	 */
//	public Message sendMail(Long idMail) throws MailServiceException, EmailStatusException;
//	

	
	
	
	
}
