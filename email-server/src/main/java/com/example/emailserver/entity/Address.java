package com.example.emailserver.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_address")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address implements Serializable {


	private static final long serialVersionUID = 8266511525375268860L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String address;
    
    @OneToMany(mappedBy = "address")
    Set<InBox> inBox;
    	
}
