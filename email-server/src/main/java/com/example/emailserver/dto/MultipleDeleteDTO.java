package com.example.emailserver.dto;

import java.util.Set;

import com.example.emailserver.enums.MailBoxType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MultipleDeleteDTO {
	
	private Long mailId;
	private Long addressId;
	private MailBoxType mailBoxType;
	private Set<Long> emailIDs;
	
	
}
