package com.leadway.leadway_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leadway.leadway_server.services.TaskService;

@RestController
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
}
