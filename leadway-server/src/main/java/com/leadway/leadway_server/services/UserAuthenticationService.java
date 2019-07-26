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

import com.leadway.leadway_server.entities.AutoLoginData;
import com.leadway.leadway_server.repositories.AutoLoginDataRepository;

@Component
public class UserAuthenticationService {
	
	@Autowired
	private EncryptionService encryptionService;
	
	@Autowired
	private AutoLoginDataRepository autoLoginDataRepository;
	
	public boolean verifyToken(String frontEndToken, HttpServletResponse httpResponse)  {
		
		String decryptedToken;
		try {
			decryptedToken = encryptionService.AESDecrypt(frontEndToken);
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException
				| NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
				| DecoderException e) {
			return false;
		}
		
		String[] splitedToken = decryptedToken.split(":");
		
//		System.out.println("Splited token 0 = " + splitedToken[0]);
//		System.out.println("Splited token 1 = " + splitedToken[1]);
		
		Long id = Long.parseLong(splitedToken[0]);
		Long token = Long.parseLong(splitedToken[1]);
		Optional<AutoLoginData> data = autoLoginDataRepository.findById(id);
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
				
				this.setTokenCookie(httpResponse, frontEndToken, rememberMe, 60 * 60);
				return true;	
			} else {
				// expired data is deleted
				autoLoginDataRepository.deleteById(currentLoginData.getId());
				return false;
			}
		}
		
		return false;
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
}
