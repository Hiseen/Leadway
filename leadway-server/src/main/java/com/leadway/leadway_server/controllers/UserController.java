package com.leadway.leadway_server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.leadway.leadway_server.entities.LeadwayUser;
import com.leadway.leadway_server.services.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService service;
	
	@RequestMapping("/welcome")
	public String welcome() {
		return service.retrieveMessage();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/add-user")
	public String addUser() {
		service.addUser();
		return "add user";
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/get-users")
	public List<LeadwayUser> getUsers() {
		return service.getUsers();
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="delete-users")
	public String deleteUsers() {
		service.deleteUsers();
		return "Users have been deleted";
	}
	
}
