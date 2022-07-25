package com.example.emailserver.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tbl_in_box")
public class InBox implements Serializable {

	private static final long serialVersionUID = 5378871332096374447L;

	@EmbeddedId
	EmailAddressKey id;

	@ManyToOne
	@MapsId("emailId")
	@JoinColumn(name = "email_id")
	Mail mail;

	@ManyToOne
	@MapsId("addressId")
	@JoinColumn(name = "address_id")
	Address address;

	public Mail getMail() {
		return mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public AddressTypeEnum getEmailAddressType() {
		return emailAddressType;
	}

	public void setEmailAddressType(AddressTypeEnum emailAddressType) {
		this.emailAddressType = emailAddressType;
	}

	public int getEmailAddressTypeValue() {
		return emailAddressTypeValue;
	}

	public void setEmailAddressTypeValue(int emailAddressTypeValue) {
		this.emailAddressTypeValue = emailAddressTypeValue;
	}

	@Transient
	private StatusEnum emailStatus;

	@Column(name = "email_status")
	private int emailStatusValue;

	@Transient
	private AddressTypeEnum emailAddressType;

	@Column(name = "address_type")
	private int emailAddressTypeValue;

	@PostLoad
	void fillTransient() {
		if (emailStatusValue > 0) {
			this.emailStatus = StatusEnum.of(emailStatusValue);
		}
		
		if (emailAddressTypeValue > 0) {
			this.emailAddressType = AddressTypeEnum.of(emailAddressTypeValue);
		}
	}

	@PrePersist
	void fillPersistent() {
		if (this.emailStatus != null) {
			this.emailStatusValue = this.emailStatus.getStatusId();
		}
		
		if (emailAddressType != null) {
			this.emailAddressTypeValue = emailAddressType.getAddressTypeId();
		}

		
	}
	
	

	public InBox() {
	}

	public InBox(Mail mail, Address address, AddressTypeEnum emailAddressType, StatusEnum status) {
		this.mail = mail;
		this.address = address;
		this.emailAddressType = emailAddressType;
		this.emailStatus =status;
		this.emailAddressTypeValue = this.emailAddressType.getAddressTypeId();
		this.emailStatusValue = this.emailStatus.getStatusId();
		this.id = new EmailAddressKey(this.mail.getId(), address.getId());
	}



}
