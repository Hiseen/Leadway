package com.leadway.leadway_server.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter @NoArgsConstructor
public class AdminUser {
	@Id
	private Long id;
	
	private String firstName;
	private String lastName;
	
	
}
