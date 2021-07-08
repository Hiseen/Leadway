package com.leadway.leadway_server.entities;

import java.time.LocalDate;

import javax.persistence.Column;
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
public class LeadwayTask {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(AccessLevel.PROTECTED)
	private Long id;
	
	private String taskName;
	private int taskType;
	
	@Column(columnDefinition="text")
	private String taskDescription;
	
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDate openDate;
	private double taskFunding;
	private double taskPenalty;
	
	private String taskStreet;
	private String taskCity;
	private String taskState;
	private double taskZip;
		
	public LeadwayTask(String taskName, int taskType, String taskDescription, LocalDate startDate, LocalDate endDate,
			LocalDate openDate, double taskFunding, double taskPenalty, String taskStreet, String taskCity,
			String taskState, double taskZip) {
		super();
		this.taskName = taskName;
		this.taskType = taskType;
		this.taskDescription = taskDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.openDate = openDate;
		this.taskFunding = taskFunding;
		this.taskPenalty = taskPenalty;
		this.taskStreet = taskStreet;
		this.taskCity = taskCity;
		this.taskState = taskState;
		this.taskZip = taskZip;
	}
	
}
