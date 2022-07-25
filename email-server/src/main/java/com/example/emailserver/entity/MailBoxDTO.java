package com.example.emailserver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MailBoxDTO {
	private String emailAddress;	
	private StatusEnum emailStatus;	
	private MailBoxType mailBoxType;
}
