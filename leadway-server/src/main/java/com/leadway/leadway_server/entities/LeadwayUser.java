package com.leadway.leadway_server.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class LeadwayUser {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(AccessLevel.PROTECTED)
	private Long id;
	
	//	(regular user 0, expert 1, enterprise 2, admin 3)
	private int type;
	private String email;
	private String password;
	private String street;
	private String city;
	private String zip;
	private String phone;
	private boolean verified;
	
	public LeadwayUser(int type, String email, String password, 
			String street, String city, String zip, String phone) {
		super();
		
		this.type = type;
		this.email = email;
		this.password = password;
		this.street = street;
		this.city = city;
		this.zip = zip;
		this.phone = phone;
	}
}
