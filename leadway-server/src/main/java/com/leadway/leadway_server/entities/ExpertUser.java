package com.leadway.leadway_server.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ExpertUser {

	@Id
	private Long id;
	
	private String firstName;
	private String lastName;
	private String experience;
	private String certification;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getCertification() {
		return certification;
	}
	public void setCertification(String certification) {
		this.certification = certification;
	}
	
	
	
}
