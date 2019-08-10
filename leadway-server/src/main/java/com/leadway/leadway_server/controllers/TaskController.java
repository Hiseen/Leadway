package com.leadway.leadway_server.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leadway.leadway_server.services.TaskService;

@RestController
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@RequestMapping(method=RequestMethod.POST, value="/api/get-task")
	public ObjectNode getTask(@RequestBody String request) throws IOException {
		
		ObjectNode requestJson = (ObjectNode) new ObjectMapper().readTree(request);
		long taskID = requestJson.get("taskID").asLong();
		
		return taskService.getTask(taskID);
	}
}
