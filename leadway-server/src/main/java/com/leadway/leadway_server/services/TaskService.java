package com.leadway.leadway_server.services;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leadway.leadway_server.entities.LeadwayTask;
import com.leadway.leadway_server.repositories.TaskRepository;

@Component
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepo;
	
	private ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
	
	/**
	 * This method creates a new task based on what administrator uploads
	 * 
	 * @param requestJson
	 * @return
	 * @throws IOException
	 */
	public ObjectNode createNewTask(String requestJson) throws IOException {
		ObjectNode result = mapper.createObjectNode();
		
		LeadwayTask newTask = mapper.readValue(requestJson, LeadwayTask.class);
		
		LocalDate currentDate = LocalDate.now();
		newTask.setOpenDate(currentDate);
		
		taskRepo.save(newTask);
		
		result.put("code", 0);
		return result;
	}
	
	/**
	 * This method list all the tasks uploaded by the administrator
	 * @return
	 */
	public ObjectNode listTasks() {
		ObjectNode result = mapper.createObjectNode();
		Iterable<LeadwayTask> allTask = taskRepo.findAll();
		
		ArrayNode tasks = mapper.valueToTree(allTask);
		result.putArray("tasks").addAll(tasks);
		return result;
	}
	
	public ObjectNode modifyTask() {
		ObjectNode result = mapper.createObjectNode();
		return result;
	}
	
	public ObjectNode deleteTask() {
		ObjectNode result = mapper.createObjectNode();
		return result;
	}
}
