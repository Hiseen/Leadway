package com.leadway.leadway_server.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EnterpriseUser {
	@Id
	private Long id;
	
	private String companyName;
	private String website;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
}
