package com.example.emailserver.service;

import java.util.Set;

import com.example.emailserver.entity.Mail;
import com.example.emailserver.entity.StatusEnum;
import com.example.emailserver.service.exception.MailServiceException;

public interface OutBoxService {

	
	/**
	 * Retrieves all mails list
	 * @return
	 */
	public Set<Mail> listEmailsFromAddresAndStatus(String address, StatusEnum status) throws MailServiceException;
	
	/**
	 * Retrieves all mails list
	 * @return
	 */
	public Set<Mail> listEmailsFromAddres(String address) throws MailServiceException;
	
	
	
}
