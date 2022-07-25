package com.example.emailserver.entity;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MultipleIdDTO {
	
	private Long mailId;
	private Set<Long> emailIDs;
	
	
}
