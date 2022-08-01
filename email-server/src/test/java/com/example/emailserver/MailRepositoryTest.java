package com.example.emailserver;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.example.emailserver.entity.Address;
import com.example.emailserver.entity.Message;
import com.example.emailserver.enums.AddressTypeEnum;
import com.example.emailserver.enums.StatusEnum;
import com.example.emailserver.repository.MailRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MailRepositoryTest {
	
	@Autowired
	private MailRepository mailRepository;
	
	private static final int NUMBER_MAILS_FROMA_A = 2;

	@Test
	public void whenFindBySender_thenReturnsListOfMails() {
		
		
		Message testMail1 = Message.builder()
				.emailBody("Hello world")
				.emailFrom("b@domain.com")
				.emailTo("a@domain.com")
				.createAt(new Timestamp(new Date().getTime()))
				.recipients(new HashSet<>()).build();
		//testMail1.addEmailAddres(addressToSet, AddressTypeEnum.TO);

		
		Message testMail2 = Message.builder()	
				.emailBody("Hello world2")
				.emailFrom("b@domain.com")
				.emailTo("a@domain.com")
				.createAt(new Timestamp(new Date().getTime()))
				.recipients(new HashSet<>()).build();

		
		mailRepository.save(testMail1);
		mailRepository.save(testMail2);
		
		List<Message> mailListFrom = mailRepository.findByEmailFrom(testMail1.getEmailFrom());
		
		Assertions.assertThat(mailListFrom.size()).isEqualTo(NUMBER_MAILS_FROMA_A);

	}
}
