package com.example.emailserver.service;

import java.util.List;
import java.util.Set;

import com.example.emailserver.entity.Mail;
import com.example.emailserver.entity.StatusEnum;
import com.example.emailserver.service.exception.EmailStatusException;
import com.example.emailserver.service.exception.MailServiceException;

public interface MailService {

	/**
	 * Retrieves all mails list
	 * @return
	 */
	public Set<Mail> listAllEmails();
	
	/**
	 * Gets mail by id
	 * @param id
	 * @return
	 */
	public Mail getMailById(Long id);
	
	/**
	 * Create a mail
	 * @param mail
	 * @return
	 */
	public Mail createMail(Mail mail) throws MailServiceException;	
	/**
	 * Update a mail
	 * @param mail
	 * @return
	 */
	public Mail updateMail(Mail mail) throws MailServiceException;
	
	/**
	 * Delete a mail
	 * @param id
	 * @return
	 */
	public Mail deleteMail(Long id);
	
	
	/**
	 * Delete multiple mails
	 * @param id
	 * @return
	 */
	public Set<Mail> deleteMail(Set<Long> id);
	
	/**
	 * Gets mails by emailfrom
	 * @param from
	 * @return
	 */
	public List<Mail> getMailByFrom(String emailAddrress);
	
	/**
	 * Update mail status
	 * @param id
	 * @param status
	 * @return
	 */
	public Mail updateStatus(Long id, StatusEnum status);
	
	/**
	 * Send a mail
	 * @param idMail
	 * @return
	 */
	public Mail sendMail(Long idMail) throws MailServiceException, EmailStatusException;
	

	
	
	
	
}
