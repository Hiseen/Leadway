package com.leadway.leadway_server.controllers;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leadway.leadway_server.services.UserService;


@RestController
public class UserController {

	@Autowired
	private UserService service;
	
	@RequestMapping(method=RequestMethod.POST, value="/register")
	public ObjectNode register(@RequestBody String request) throws IOException, InvalidKeySpecException {		
		ObjectNode signUpForm = (ObjectNode) new ObjectMapper().readTree(request);
		System.out.println("Sign Up");
		System.out.println(signUpForm);
		return service.createNewUserEntities(signUpForm);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/login")
	public ObjectNode signIn(@RequestBody String request) throws IOException, InvalidKeySpecException {		
		ObjectNode signInForm = (ObjectNode) new ObjectMapper().readTree(request);
		System.out.println("Sign In");
		System.out.println(signInForm);
		return service.loginUser(signInForm);
	}

	@RequestMapping(method=RequestMethod.GET, value="/verify")
	public ObjectNode verifyUser(@RequestParam("code") String code){
		return service.verifyUser(code);
	}
	
}
