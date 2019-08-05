package com.leadway.leadway_server.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LeadwayTask {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
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
	
	public LeadwayTask() { }

	
	public LeadwayTask(String taskName, int taskType, String taskDescription, LocalDate startDate, LocalDate endDate,
			LocalDate openDate, double taskFunding, double taskPenalty) {
		super();
		this.taskName = taskName;
		this.taskType = taskType;
		this.taskDescription = taskDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.openDate = openDate;
		this.taskFunding = taskFunding;
		this.taskPenalty = taskPenalty;
	}


	public LocalDate getOpenDate() {
		return openDate;
	}

	public void setOpenDate(LocalDate openDate) {
		this.openDate = openDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public double getTaskFunding() {
		return taskFunding;
	}
	public void setTaskFunding(double taskFunding) {
		this.taskFunding = taskFunding;
	}
	public double getTaskPenalty() {
		return taskPenalty;
	}
	public void setTaskPenalty(double taskPenalty) {
		this.taskPenalty = taskPenalty;
	} 

	
	
}
