package com.example.emailserver.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class EmailAddressKey implements Serializable{

	private static final long serialVersionUID = -8962284758773822961L;

	public EmailAddressKey(){}
	
	@Column(name = "email_id")
    Long emailId;

    @Column(name = "address_id")
    Long addressId;
	
	
}
