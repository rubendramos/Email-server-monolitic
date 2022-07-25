package com.example.emailserver;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.emailserver.entity.Mail;
import com.example.emailserver.entity.StatusEnum;
import com.example.emailserver.repository.AddressRepository;
import com.example.emailserver.repository.InBoxRepository;
import com.example.emailserver.repository.MailRepository;
import com.example.emailserver.repository.OutBoxRepository;
import com.example.emailserver.service.MailService;
import com.example.emailserver.service.MailServiceImpl;

@SpringBootTest
public class MailServiceMockTest {

	@Mock
	private MailRepository mailRepository;
	
	@Mock
	private AddressRepository addressRepository;
	
	@Mock
	private OutBoxRepository outBoxRepository;
	
	@Mock
	private InBoxRepository inBoxRepository;
	
	private MailService mailService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mailService = new MailServiceImpl(mailRepository,addressRepository, outBoxRepository, inBoxRepository);	
		
		Mail testMail1 = Mail.builder()
				.emailBody("Hello world")
				.emailFrom("a@domain.com")				
				.emailTo("a@domain.com")
				.createAt(new Timestamp(new Date().getTime())).build();
		//testMail1.addEmailAddres(addressToSet, AddressTypeEnum.TO);
		
		Mockito.when(mailRepository.findById(1L))
		.thenReturn(Optional.of(testMail1));
		
		Mockito.when(mailRepository.save(testMail1))
		.thenReturn(testMail1);
		
	}
	
	@Test
	public void whenValidGetId_thenReturnMail() {
		Mail foundMail = mailService.getMailById(1L);
		Assertions.assertThat(foundMail.getEmailBody()).isEqualTo("Hello world");
	}
	
	@Test
	public void whenValidUpdateStatus_thenReturnNewStatus() {
		Mail newStatusMail = mailService.updateStatus(1L, StatusEnum.SPAN);
		Assertions.assertThat(newStatusMail.getOutBox().getEmailStatus()).isEqualTo(StatusEnum.SPAN);
		
		
	}
}
