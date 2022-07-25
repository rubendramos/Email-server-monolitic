package com.example.emailserver.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tbl_out_box")
public class OutBox implements Serializable {

	private static final long serialVersionUID = 4301696990442367147L;

	@Id
    @Column(name = "email_id")
    private Long id;
	
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name="email_id")
    private Mail mail;
	
    @OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	Address address;

	
	public OutBox() {
	}

	public OutBox(Mail mail, Address address, StatusEnum status) {
		this.mail = mail;
		this.address = address;
		this.emailStatus =status;
	}
	

	@Transient
	private StatusEnum emailStatus;

	@Column(name = "email_status")
	private int emailStatusValue;


	@PostLoad
	void fillTransient() {
		if (emailStatusValue > 0) {
			this.emailStatus = StatusEnum.of(emailStatusValue);
		}
		
	}

	@PrePersist
	void fillPersistent() {
		if (this.emailStatus != null) {
			this.emailStatusValue = this.emailStatus.getStatusId();
		}	
	}


	public StatusEnum getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(StatusEnum emailStatus) {
		this.emailStatus = emailStatus;
	}

	public int getEmailStatusValue() {
		return emailStatusValue;
	}

	public void setEmailStatusValue(int emailStatusValue) {
		this.emailStatusValue = emailStatusValue;
	}

	public Long getId() {
		return id;
	}

	public Mail getMail() {
		return mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	public Address getEmailAddress() {
		return address;
	}

	public void setEmailAddress(Address addressId) {
		this.address = addressId;
	}


}
