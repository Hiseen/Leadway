package com.leadway.leadway_server.services;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;
import java.util.Optional;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.MessagingException;

import org.apache.commons.codec.DecoderException;
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
	private IvParameterSpec iv = null;
	private SecretKey secretKey;
	private SecretKeyFactory factory;
	private int bcryptIterations = 65536;
	private int keyLength = 128;
	
	public UserService() throws NoSuchAlgorithmException {
		// generating PBKDF2 salts and factory
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		// generating AES related credentials
		KeyGenerator keyGen=KeyGenerator.getInstance("AES");
		keyGen.init(128);
		secretKey = keyGen.generateKey();
		byte[] ivbytes = new byte[16];
		random.nextBytes(ivbytes);
		iv=new IvParameterSpec(ivbytes);
	}
	
	public ObjectNode createNewUserEntities(ObjectNode signUpForm) throws InvalidKeySpecException, MessagingException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
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
			Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			byte[] byteCipherText = aesCipher.doFinal(newUser.getId().toString().getBytes());
			mailService.sendVerificationMailTo(userEmail, Hex.encodeHexString(byteCipherText));
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

	public ObjectNode verifyUser(String code) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, DecoderException, BadPaddingException, IllegalBlockSizeException {
		ObjectNode result = new ObjectMapper().createObjectNode();
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		aesCipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		String decrypted=new String(aesCipher.doFinal(Hex.decodeHex(code.toCharArray())));
		Long id;
		try {
			id=Long.parseLong(decrypted);
		}
		catch(Exception ex) {
			result.put("code",1);
			result.put("error","invalid code received! code = "+code);
			return result;
		}
		System.out.println("verify user with id = "+id);
		Optional<LeadwayUser> foundUser=userRepository.findById(id);
		if (!foundUser.isPresent()) {
			result.put("code", 1);
			result.put("error","no user found! code = "+code);
		} else{
			LeadwayUser user=foundUser.get();
			if(user.getType()!=0) {
				result.put("error","user already verified! code = "+code);
				result.put("code", 1);
			} else{
				user.setType(1);
				userRepository.save(user);
				System.out.println("user verified! code = "+code);
				result.put("code", 0);
			}
		}
		return result;
	}
}
