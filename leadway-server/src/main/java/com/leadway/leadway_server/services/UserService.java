package com.leadway.leadway_server.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leadway.leadway_server.entities.LeadwayUser;
import com.leadway.leadway_server.repositories.UserRepository;

@Component
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public String retrieveMessage() {
		return "User service message using auto wiring";
	}
	
	public void addUser() {
		LeadwayUser newUser = new LeadwayUser(
				0, "henry@google.com", "henrypassword",
				"henryStreet", "henryCity", "henryZip", "henryPhone");
		
		userRepository.save(newUser);
	}
	
	public List<LeadwayUser> getUsers() {
		List<LeadwayUser> resultList = new ArrayList<LeadwayUser> ();
		userRepository.findAll().forEach(user -> {
			resultList.add(user);
		});
		
		return resultList;
	}
	
	public void deleteUsers() {
		userRepository.deleteAll();
	}
}
