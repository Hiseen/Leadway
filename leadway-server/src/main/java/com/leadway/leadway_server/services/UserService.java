package com.leadway.leadway_server.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.crypto.*;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.leadway.leadway_server.constants.LeadwayConstants;
import com.leadway.leadway_server.entities.AdminUser;
import com.leadway.leadway_server.entities.AutoLoginData;
import com.leadway.leadway_server.entities.EnterpriseUser;
import com.leadway.leadway_server.entities.ExpertUser;
import com.leadway.leadway_server.repositories.AdminUserRepository;
import com.leadway.leadway_server.repositories.AutoLoginDataRepository;
import com.leadway.leadway_server.repositories.EnterpriseUserRepository;
import com.leadway.leadway_server.repositories.ExpertUserRepository;
import com.leadway.leadway_server.repositories.RegularUserRepository;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leadway.leadway_server.entities.LeadwayUser;
import com.leadway.leadway_server.entities.RegularUser;
import com.leadway.leadway_server.repositories.UserRepository;

@Component
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RegularUserRepository regularUserRepo;
	
	@Autowired
	private ExpertUserRepository expertUserRepo;
	
	@Autowired
	private AdminUserRepository adminUserRepo;
	
	@Autowired
	private EnterpriseUserRepository enterpriseUserRepo;

	@Autowired
	private AutoLoginDataRepository autoLoginDataRepository;

	@Autowired
	private MailService mailService;

	@Autowired
	private EncryptionService encryptionService;
	
	@Autowired
	private CookieService cookieService;
	
	
	// temporary administrator password
	private String adminPassword = "123leadway123";

	private UserService() {}
	
	public ObjectNode createNewUserEntities(ObjectNode signUpForm) {
		ObjectNode result = new ObjectMapper().createObjectNode();

		LeadwayUser newUser = new LeadwayUser();
		String userEmail = signUpForm.get("email").asText();
		
		List<LeadwayUser> userWithIdenticalEmail = userRepository.findByEmail(userEmail);


		if (userWithIdenticalEmail.size() > 0) {
			// if email is already used and verified previously, mark this operation is failed
			LeadwayUser user = userWithIdenticalEmail.get(0);
			if (user.isVerified()) {
				result.put("code", 1);
				result.put("error", "Email already used");
				return result;				
			}
		}
		
		int userType = signUpForm.get("customertype").asInt();
		// administrator
		if (userType == 3) {
			String adminCode = signUpForm.get("admincode").asText();
			if (!adminPassword.equals(adminCode)) {
				result.put("code", 1);
				result.put("error", "Administrator code is incorrect");
				return result;	
			}
		}


		String userPassword = signUpForm.get("password").asText();
		String encryptedPassword;
		try {
			encryptedPassword = encryptionService.PBKDF2Encrypt(userPassword);
			newUser.setPassword(encryptedPassword);

		} catch (InvalidKeySpecException e) {
			result.put("code", 1);
			result.put("error", "PBKDF2 encryption invalid key error");
			return result;	
		}
		newUser.setEmail(userEmail);
		newUser.setType(userType);
		
		newUser.setVerified(false);
		
		if (LeadwayConstants.isDeveloping) {
			newUser.setVerified(true);
		}
		
		userRepository.save(newUser);
		
		// generates user entity based on the user type (regular, expert, enterprise, admin)
		this.generateUserType(newUser.getId(), userType, signUpForm);

		if (!LeadwayConstants.isDeveloping) {
			try {
				mailService.sendVerificationMailTo(userEmail, encryptionService.AESEncrypt(newUser.getId().toString()));
			} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException
					| UnsupportedEncodingException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
					| NoSuchPaddingException | MessagingException e) {
				result.put("code", 1);
				result.put("error", "mailservice email verification error");
				return result;	
			}			
		}
		
		result.put("code", 0);
		return result;	
		
	}
	
	/**
	 * This method generates the user entity type in the database
	 * 	based on the user type.
	 * 
	 * @param signUpForm
	 */
	private void generateUserType(long userID, int userType, ObjectNode signUpForm) {
		if (userType == 0) {
			RegularUser newUser = new RegularUser();
			newUser.setId(userID);
			newUser.setFirstName(signUpForm.get("firstname").asText());
			newUser.setLastName(signUpForm.get("lastname").asText());
			regularUserRepo.save(newUser);
		} else if (userType == 1) {
			ExpertUser newUser = new ExpertUser();
			newUser.setId(userID);
			newUser.setFirstName(signUpForm.get("firstname").asText());
			newUser.setLastName(signUpForm.get("lastname").asText());
			newUser.setExperience(signUpForm.get("experience").asText());
			newUser.setCertification(signUpForm.get("certification").asText());
			expertUserRepo.save(newUser);
		} else if (userType == 2) {
			EnterpriseUser newUser = new EnterpriseUser();
			newUser.setId(userID);
			newUser.setCompanyName(signUpForm.get("companyname").asText());
			newUser.setWebsite(signUpForm.get("webaddress").asText());
			enterpriseUserRepo.save(newUser);
		} else {
			AdminUser newUser = new AdminUser();
			newUser.setId(userID);
			newUser.setFirstName(signUpForm.get("firstname").asText());
			newUser.setLastName(signUpForm.get("lastname").asText());
			adminUserRepo.save(newUser);
		}
	}
	
	public ObjectNode loginUser(ObjectNode signInForm, HttpServletResponse httpResponse) throws InvalidKeySpecException, 
			BadPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		
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
			if (!LeadwayConstants.isDeveloping && !foundUser.isVerified()) {
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
			result.put("token", saltedToken);
			
			// set cookie for the request, 60 * 60 = 1 hour (only for not remembered cookie)
			int cookieExpiryDate = 60 * 60;
			cookieService.setTokenCookie(httpResponse, saltedToken, rememberMe, cookieExpiryDate);

			result.set("userInfo", this.gatherLoginInfo(foundUser));
			
			return result;
		}
	}
	
	/**
	 * This function will gather the information needed for the frontend
	 * 	when the user login into the leadway main platform.
	 * 
	 * @param user
	 * @return
	 */
	public ObjectNode gatherLoginInfo(LeadwayUser user) {
		ObjectNode userInfoObject = new ObjectMapper().createObjectNode();
		String email = user.getEmail();
		int type = user.getType();
		Long userID = user.getId();
		
		StringBuilder nameBuilder = new StringBuilder();
		if (type == 2) {
			Optional<EnterpriseUser> ent = enterpriseUserRepo.findById(userID);
			if (!ent.isPresent()) {
				throw new Error("Enterprise cannot be found during signin");
			}
			nameBuilder.append(ent.get().getCompanyName());
		} else {
			if (type == 0) {
				Optional<RegularUser> reg = regularUserRepo.findById(userID);
				if (!reg.isPresent()) {
					throw new Error("Regular user cannot be found during signin");
				}
				RegularUser regUser = reg.get();
				nameBuilder.append(regUser.getFirstName() + " " + regUser.getLastName());
			} else if (type == 1) {
				Optional<ExpertUser> expert = expertUserRepo.findById(userID);
				if (!expert.isPresent()) {
					throw new Error("Regular user cannot be found during signin");
				}
				ExpertUser expertUser = expert.get();
				nameBuilder.append(expertUser.getFirstName() + " " + expertUser.getLastName());
			} else {
				Optional<AdminUser> admin = adminUserRepo.findById(userID);
				if (!admin.isPresent()) {
					throw new Error("Regular user cannot be found during signin");
				}
				AdminUser adminUser = admin.get();
				nameBuilder.append(adminUser.getFirstName() + " " + adminUser.getLastName());
			}
		}
		
		
		userInfoObject.put("email", email);
		userInfoObject.put("type", type);
		userInfoObject.put("name", nameBuilder.toString());
		
		// all requests afterwards uses userID (and use cookie for verification)
		userInfoObject.put("userID", userID);

		return userInfoObject;
	}

	public ObjectNode verifyUser(String code) throws BadPaddingException, IllegalBlockSizeException, 
			DecoderException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, 
			NoSuchPaddingException, InvalidAlgorithmParameterException {
		ObjectNode result = new ObjectMapper().createObjectNode();
		String decrypted = encryptionService.AESDecrypt(code);
		System.out.println("IN VERIFY, encrypted = " + code);
		System.out.println("IN VERIFY, decrypted = " + decrypted);
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
		Optional<LeadwayUser> foundUser = userRepository.findById(id);
		if (!foundUser.isPresent()) {
			result.put("code", 1);
			result.put("error","no user found! code = " + code);
		} else{
			LeadwayUser user = foundUser.get();
			if(user.isVerified()) {
				result.put("error","user already verified! code = " + code);
				result.put("code", 1);
			} else{
				user.setVerified(true);
				userRepository.save(user);
				System.out.println("user verified! code = "+code);
				result.put("code", 0);
				result.put("message", "User successfully verified! You can login to leadway right now!");
			}
		}
		return result;
	}
	
	/**
	 * Logout from Leadway platform by removing the entity in the auto login repository
	 * 	and removing the cookie stored inside the http response.
	 * 
	 * @param logoutInfo
	 * @param httpResponse
	 * @return
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws DecoderException
	 */
	public ObjectNode logoutUser(ObjectNode logoutInfo, HttpServletResponse httpResponse) 
			throws BadPaddingException, IllegalBlockSizeException, DecoderException {
		ObjectNode result = new ObjectMapper().createObjectNode();
		long userID = logoutInfo.get("id").asLong();
				
		autoLoginDataRepository.deleteById(userID);
		cookieService.removeTokenCookie(httpResponse, "");
		
		result.put("code", 0);
		return result;
	}
}
