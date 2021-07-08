package com.leadway.leadway_server.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class EnterpriseUser {
	@Id
	private Long id;
	
	private String companyName;
	private String website;
}
