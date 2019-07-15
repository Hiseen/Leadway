package com.leadway.leadway_server.controllers;

import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leadway.leadway_server.services.UserAuthenticationService;

@RestController
public class LoginAuthenticationController {

	@Autowired
	private UserAuthenticationService authService;
	
	@RequestMapping(method=RequestMethod.POST, value="/user-auth")
	public boolean verifySessionToken(@RequestBody String request) throws IOException, 
			BadPaddingException, IllegalBlockSizeException, DecoderException {
		ObjectNode requestJson = (ObjectNode) new ObjectMapper().readTree(request);
		JsonNode userToken = requestJson.get("token");
		return authService.verifyToken(userToken.asText());
	}
}
