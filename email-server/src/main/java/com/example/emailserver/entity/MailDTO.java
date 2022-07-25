package com.example.emailserver.entity;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MailDTO {

private Long mailId;
private String emailFrom;
private Set<String> emailTo;
private Set<String> emailCc;
private String emailBody;
private Date createAt;
private Date updateAt;
private StatusEnum emailStatus;

}
