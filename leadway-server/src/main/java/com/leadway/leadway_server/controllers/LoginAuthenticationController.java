package com.leadway.leadway_server.controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class LoginAuthenticationController {

	
	@RequestMapping(method=RequestMethod.POST, value="/user-auth")
	public boolean verifySessionToken(@RequestBody String request) throws IOException {
		ObjectNode requestJson = (ObjectNode) new ObjectMapper().readTree(request);
		
		return true;
	}
}
