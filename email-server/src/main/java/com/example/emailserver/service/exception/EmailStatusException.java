package com.example.emailserver.service.exception;

public class EmailStatusException extends MailServiceException{


	private static final long serialVersionUID = 1L;

	public EmailStatusException(Exception exception, Object[] paramsException) {
		super(exception, paramsException);
	}
	
	
	public EmailStatusException(String message, Object[] paramsException) {
		super(message, paramsException);
	}

}
