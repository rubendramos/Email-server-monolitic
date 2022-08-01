package com.example.emailserver.entity;

import com.example.emailserver.enums.StatusEnum;

public interface Email {
	

	public Message getMessage(); 

	public Address getAddress();
	
	public StatusEnum getEmailStatus(); 
	
	public int getEmailStatusValue();
	
	public void setAddress(Address address);
	
	public void setEmailStatus(StatusEnum statusEnum);
	
	public void setMessage(Message message); 
	
	public void setEmailStatusValue(int emailStatusValue);

	
}
