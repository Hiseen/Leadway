package com.leadway.leadway_server.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leadway.leadway_server.entities.LeadwayUser;
import com.leadway.leadway_server.repositories.UserRepository;

@Component
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	private byte[] salt = new byte[16];
	private SecretKeyFactory factory;
	private int bcryptIterations = 65536;
	private int keyLength = 128;
	
	public UserService() throws NoSuchAlgorithmException {
		// generating PBKDF2 salts and factory
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	}
	
	public ObjectNode createNewUserEntities(ObjectNode signUpForm) throws InvalidKeySpecException {
		ObjectNode result = new ObjectMapper().createObjectNode();

		LeadwayUser newUser = new LeadwayUser();
		String userEmail = signUpForm.get("email").asText();
		
		List<LeadwayUser> userWithIdenticalEmail = userRepository.findByEmail(userEmail);
		
		System.out.println(signUpForm);
		if (userWithIdenticalEmail.size() > 0) {
			// if email is already used previously, mark this operation is failed
			result.put("code", 1);
			System.out.println("Email already used");
			return result;
		} else {
			
			String userPassword = signUpForm.get("password").asText();
			
			// conduct PBKDF2 encryption that uses `salt`
			KeySpec spec = new PBEKeySpec(userPassword.toCharArray(), salt, bcryptIterations, keyLength);
			byte[] hash = factory.generateSecret(spec).getEncoded();
			
			String encryptedPassword = Hex.encodeHexString(hash);
			
			newUser.setEmail(userEmail);
			newUser.setPassword(encryptedPassword);
			
			userRepository.save(newUser);
			
			result.put("code", 0);
			System.out.println("Email registered");

			return result;	
		}
	}
	
	public ObjectNode loginUser(ObjectNode signInForm) throws InvalidKeySpecException {
		ObjectNode result = new ObjectMapper().createObjectNode();

		String userEmail = signInForm.get("email").asText();
		String userPassword = signInForm.get("password").asText();
		
		// conduct PBKDF2 encryption that uses `salt`
		KeySpec spec = new PBEKeySpec(userPassword.toCharArray(), salt, bcryptIterations, keyLength);
		byte[] hash = factory.generateSecret(spec).getEncoded();
		
		String encryptedPassword = Hex.encodeHexString(hash);
		
		List<LeadwayUser> users = userRepository.findByEmailAndPassword(userEmail, encryptedPassword);
		if (users.size() == 0) {
			result.put("code", 1);
			return result;
		} else {
			LeadwayUser foundUser = users.get(0);
			JsonNode userJson = new ObjectMapper().valueToTree(foundUser);
			result.put("code", 0);
			result.set("user", userJson);
			return result;
		}
	}
	
	
	// testings
	
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
