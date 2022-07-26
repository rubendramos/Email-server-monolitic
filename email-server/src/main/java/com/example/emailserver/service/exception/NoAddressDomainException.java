package com.example.emailserver.service.exception;

public class NoAddressDomainException extends MailServiceException{


	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_EXCEPTION = "The email adrress not belongs to de current domain";

	public NoAddressDomainException(Exception exception, Object[] paramsException) {
		super(exception, paramsException);
	}
	
	
	public NoAddressDomainException(String message, Object[] paramsException) {
		super(message, paramsException);
	}
	
	
	public NoAddressDomainException(Object[] paramsException) {
		super(MESSAGE_EXCEPTION, paramsException);
	}

}
