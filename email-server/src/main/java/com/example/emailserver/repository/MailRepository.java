package com.example.emailserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailserver.entity.Message;

public interface MailRepository extends JpaRepository<Message, Long>{

	public List<Message> findByEmailFrom(String emailAddrress);
}
