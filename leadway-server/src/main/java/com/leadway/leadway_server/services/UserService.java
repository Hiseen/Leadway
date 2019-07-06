package com.leadway.leadway_server.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.MessagingException;

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

	@Autowired
	private MailService mailService;
	
	private byte[] salt = new byte[16];
	private SecretKeyFactory factory;
	private int bcryptIterations = 65536;
	private int keyLength = 128;
	private final long frontMask=2478915738594627173L;
	private final long backMask=7592345738492835728L;
	
	public UserService() throws NoSuchAlgorithmException {
		// generating PBKDF2 salts and factory
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	}
	
	public ObjectNode createNewUserEntities(ObjectNode signUpForm) throws InvalidKeySpecException, MessagingException {
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
			long privateKey=System.currentTimeMillis();
			newUser.setPrivateKey(privateKey);
			userRepository.save(newUser);
			UUID uuid=new UUID(newUser.getId()^frontMask,privateKey^backMask);
			mailService.sendVerificationMailTo(userEmail, uuid);
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

	public ObjectNode verifyUser(String code) {
		ObjectNode result = new ObjectMapper().createObjectNode();
		UUID uuid;
		try {
			uuid = UUID.fromString(code);
		}
		catch(NumberFormatException e) {
			result.put("code",1);
			System.out.println("invalid code received! code = "+code);
			return result;
		}
		long id=uuid.getMostSignificantBits()^frontMask;
		long privateKey=uuid.getLeastSignificantBits()^backMask;
		List<LeadwayUser> users=userRepository.findById(id);
		if (users == null || users.size() == 0) {
			result.put("code", 1);
			System.out.println("no user found! code = "+code);
		} else if(users.size()>1){
			result.put("code", 1);
			System.out.println("more than 1 user found! code = "+code);
		}
		else{
			LeadwayUser foundUser = users.get(0);
			if(foundUser.getType()!=0) {
				System.out.println("user already verified! code = "+code);
				result.put("code", 1);
			}else if(foundUser.getPrivateKey()!=privateKey) {
				System.out.println("database is being attacked!!! code = " + code);
				result.put("code", 1);
			}
			else{
				foundUser.setType(1);
				userRepository.save(foundUser);
				System.out.println("user verified! code = "+code);
				result.put("code", 0);
			}
		}
		return result;
	}
}
