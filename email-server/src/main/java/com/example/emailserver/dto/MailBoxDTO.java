package com.example.emailserver.dto;

import com.example.emailserver.enums.MailBoxType;
import com.example.emailserver.enums.StatusEnum;

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
