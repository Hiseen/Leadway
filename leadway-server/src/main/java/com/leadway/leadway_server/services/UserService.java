package com.leadway.leadway_server.services;

import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.crypto.*;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.leadway.leadway_server.entities.AutoLoginData;
import com.leadway.leadway_server.repositories.AutoLoginDataRepository;
import org.apache.commons.codec.DecoderException;
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
	private AutoLoginDataRepository autoLoginDataRepository;

	@Autowired
	private MailService mailService;

	@Autowired
	private EncryptionService encryptionService;
	
	// temporary variable to prevent email authentication every single time
	private boolean isDeveloping = true;

	private UserService() {}
	
	public ObjectNode createNewUserEntities(ObjectNode signUpForm) throws InvalidKeySpecException, MessagingException, 
			BadPaddingException, IllegalBlockSizeException {
		ObjectNode result = new ObjectMapper().createObjectNode();

		LeadwayUser newUser = new LeadwayUser();
		String userEmail = signUpForm.get("email").asText();
		
		List<LeadwayUser> userWithIdenticalEmail = userRepository.findByEmail(userEmail);


		if (userWithIdenticalEmail.size() > 0) {
			// if email is already used and verified previously, mark this operation is failed
			LeadwayUser user = userWithIdenticalEmail.get(0);
			if (user.getType() != 0) {
				result.put("code", 1);
				result.put("error", "Email already used");
				return result;
			}
		}
			
		String userPassword = signUpForm.get("password").asText();
		String encryptedPassword = encryptionService.PBKDF2Encrypt(userPassword);
		newUser.setEmail(userEmail);
		newUser.setPassword(encryptedPassword);
		
		// type 0 = unverified user
		newUser.setType(0);
		userRepository.save(newUser);
		
		if (!isDeveloping) {
			mailService.sendVerificationMailTo(userEmail, encryptionService.AESEncrypt(newUser.getId().toString()));			
		}
		
		result.put("code", 0);
		result.put("error", "user registered, please verify your account through email");
		
		return result;	
		
	}
	
	public ObjectNode loginUser(ObjectNode signInForm, HttpServletResponse httpResponse) throws InvalidKeySpecException, 
			BadPaddingException, IllegalBlockSizeException {
		
		ObjectNode result = new ObjectMapper().createObjectNode();
		String userEmail = signInForm.get("email").asText();
		String userPassword = signInForm.get("password").asText();
		String encryptedPassword = encryptionService.PBKDF2Encrypt(userPassword);
		boolean rememberMe = signInForm.get("rememberMe").asBoolean();
		
		List<LeadwayUser> users = userRepository.findByEmailAndPassword(userEmail, encryptedPassword);
		
		if (users.size() == 0) {
			result.put("code", 1);
			result.put("error", "invalid username or password");
			return result;
		} else {
			LeadwayUser foundUser = users.get(0);
			if (!isDeveloping && foundUser.getType() == 0) {
				result.put("code", 1);
				result.put("error", "account is not verified yet");
				return result;
			}
			
			JsonNode userJson = new ObjectMapper().valueToTree(foundUser);
			result.put("code", 0);
			
			// token lives 7 days if remember is selected, else 1 hours
			long expirationTime = rememberMe ? TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS) : 
				TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
			
			Long token = new SecureRandom().nextLong();
			String tokenString = foundUser.getId() + ":" + token.toString();
			String saltedToken = encryptionService.AESEncrypt(tokenString);
			
			System.out.println("Sign IN token = " + saltedToken);

			
			Optional<AutoLoginData> foundData = autoLoginDataRepository.findById(foundUser.getId());
			AutoLoginData data = new AutoLoginData();
			if (foundData.isPresent()) {
				data = foundData.get();
			} else {
				data.setUser(foundUser);
			}
			data.setToken(token);
			data.setExpirationTime(System.currentTimeMillis() + expirationTime);
			data.setRemember(rememberMe);
			autoLoginDataRepository.save(data);
			result.put("data", saltedToken);
			
			// set cookie for the request, 60 * 60 = 1 hour (only for not remembered cookie)
			int cookieExpiryDate = 60 * 60;
			this.setTokenCookie(httpResponse, saltedToken, rememberMe, cookieExpiryDate);

			return result;
		}
	}

	public ObjectNode verifyUser(String code) throws BadPaddingException, IllegalBlockSizeException, DecoderException {
		ObjectNode result = new ObjectMapper().createObjectNode();
		String decrypted = encryptionService.AESDecrypt(code);
		Long id;
		try {
			id=Long.parseLong(decrypted);
		}
		catch(Exception ex) {
			result.put("code",1);
			result.put("error","invalid code received! code = " + code);
			return result;
		}
		System.out.println("verify user with id = "+id);
		Optional<LeadwayUser> foundUser=userRepository.findById(id);
		if (!foundUser.isPresent()) {
			result.put("code", 1);
			result.put("error","no user found! code = " + code);
		} else{
			LeadwayUser user = foundUser.get();
			if(user.getType() != 0) {
				result.put("error","user already verified! code = " + code);
				result.put("code", 1);
			} else{
				user.setType(1);
				userRepository.save(user);
				System.out.println("user verified! code = "+code);
				result.put("code", 0);
				result.put("message", "User successfully verified! You can login to leadway right now!");
			}
		}
		return result;
	}
	
	public ObjectNode logoutUser(ObjectNode logoutInfo, HttpServletResponse httpResponse) 
			throws BadPaddingException, IllegalBlockSizeException, DecoderException {
		ObjectNode result = new ObjectMapper().createObjectNode();
		String saltedToken = logoutInfo.get("token").asText();
		String decryptedToken =  encryptionService.AESDecrypt(saltedToken);
		String[] splitedToken = decryptedToken.split(":");
		
		Long id = Long.parseLong(splitedToken[0]);
		
		autoLoginDataRepository.deleteById(id);
		this.removeTokenCookie(httpResponse, saltedToken);
		
		result.put("code", 0);
		
		return result;
	}

	private void setTokenCookie(HttpServletResponse httpResponse, String token, boolean rememberMe, int expiry) {
		Cookie cookie = new Cookie("token", token);
		// if remember me is not selected when login, cookie will expire, else the cookie is permanent
		if (!rememberMe) {
			cookie.setMaxAge(expiry);			
		}
		//cookie.setSecure(true);  // requires HTTPS?
		cookie.setPath("/"); // work for whole domain
		httpResponse.addCookie(cookie);
	}
	
	private void removeTokenCookie(HttpServletResponse httpResponse, String token) {
		Cookie cookie = new Cookie("token", token);
		cookie.setMaxAge(0);		
		cookie.setPath("/"); // work for whole domain
		httpResponse.addCookie(cookie);
	}
}
