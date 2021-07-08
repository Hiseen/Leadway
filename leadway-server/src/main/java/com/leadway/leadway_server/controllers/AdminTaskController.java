package com.leadway.leadway_server.controllers;

import java.io.IOException;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leadway.leadway_server.services.TaskService;

@RestController
public class AdminTaskController {
	
	@Autowired
	private TaskService taskService;
	

	@RequestMapping(method=RequestMethod.POST, value="/api/admin-create")
	public ObjectNode addTask(@RequestBody String request) throws JsonParseException, JsonMappingException, IOException {
		return taskService.createNewTask(request);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/api/admin-list")
	public ObjectNode listTasks() {
		return taskService.listTasks();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/api/admin-delete")
	public ObjectNode deleteTask(@RequestBody String request) throws IOException {
		ObjectNode requestJson = (ObjectNode) new ObjectMapper().readTree(request);
		long deleteTaskID = requestJson.get("taskId").asLong();
		return taskService.deleteTask(deleteTaskID);
	}
	
//	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.findAndRegisterModules();
//		String request = "{\"taskFunding\":\"12\",\"taskPenalty\":\"12\",\"startDate\":\"2019-09-29T07:00:00.000Z\",\"endDate\":\"2019-08-29T07:00:00.000Z\",\"taskDescription\":null,\"taskType\":\"1\",\"taskName\":\"HenruSY\"}";
//		LeadwayTask newTask = mapper.readValue(request, LeadwayTask.class);
//		System.out.println(mapper.writeValueAsString(newTask));
//		System.out.println(newTask.getStartDate());
//		System.out.println(newTask.getEndDate());
//
//		long days = ChronoUnit.DAYS.between(newTask.getStartDate(), newTask.getEndDate());
//		System.out.println("Difference = " + days);
//	}
}
