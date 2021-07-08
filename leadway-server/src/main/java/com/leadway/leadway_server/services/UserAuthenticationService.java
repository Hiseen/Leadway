package com.leadway.leadway_server.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leadway.leadway_server.entities.AutoLoginData;
import com.leadway.leadway_server.entities.LeadwayUser;
import com.leadway.leadway_server.repositories.AutoLoginDataRepository;
import com.leadway.leadway_server.repositories.UserRepository;

@Component
public class UserAuthenticationService {
	
	@Autowired
	private EncryptionService encryptionService;
	
	@Autowired
	private AutoLoginDataRepository autoLoginDataRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CookieService cookieService;
	
	@Autowired
	private UserService userService;
	
	public ObjectNode verifyToken(String frontEndToken, HttpServletResponse httpResponse)  {
		
		ObjectNode result = new ObjectMapper().createObjectNode();
		
		String decryptedToken;
		try {
			decryptedToken = encryptionService.AESDecrypt(frontEndToken);
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException
				| NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
				| DecoderException e) {
			
			result.put("verified", false);
			return result;
		}
		
		String[] splitedToken = decryptedToken.split(":");
		
		
		Long id = Long.parseLong(splitedToken[0]);
		Long token = Long.parseLong(splitedToken[1]);
		Optional<AutoLoginData> data = autoLoginDataRepository.findById(id);
		
		Optional<LeadwayUser> userInfo = userRepository.findById(id);
		if (!userInfo.isPresent()) {
			result.put("verified", false);
			return result;
		}
		
		LeadwayUser user = userInfo.get();
		
		if (data.isPresent() && data.get().getToken().equals(token)) {
			AutoLoginData currentLoginData = data.get();
			if (data.get().getExpirationTime() > System.currentTimeMillis()) {
				//found data and it does not expire.

				// update token time whenever user uses it;
				boolean rememberMe = currentLoginData.isRemember();
				// token lives 7 days if remember is selected, else 1 hours
				long expirationTime = rememberMe ? TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS) : 
					TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
				
				currentLoginData.setExpirationTime(System.currentTimeMillis() + expirationTime);
				autoLoginDataRepository.save(currentLoginData);
				
				cookieService.setTokenCookie(httpResponse, frontEndToken, rememberMe, 60 * 60);

				result.put("verified", true);
				result.set("userInfo", userService.gatherLoginInfo(user));
				return result;
			} else {
				// expired data is deleted
				autoLoginDataRepository.deleteById(currentLoginData.getId());

				result.put("verified", false);
				return result;
			}
		}
		

		result.put("verified", false);
		return result;
	}
}
