package com.example.emailserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailserver.entity.Mail;

public interface MailRepository extends JpaRepository<Mail, Long>{

	public List<Mail> findByEmailFrom(String emailAddrress);
}
