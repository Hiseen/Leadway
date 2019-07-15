package com.leadway.leadway_server.services;

import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

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
	
	public boolean verifyToken(String frontEndToken) throws BadPaddingException, IllegalBlockSizeException, DecoderException {
		
		String decryptedToken = encryptionService.AESDecrypt(frontEndToken);
		String[] splitedToken = decryptedToken.split(":");
		Long id = Long.parseLong(splitedToken[0]);
		Long token = Long.parseLong(splitedToken[1]);
		Optional<AutoLoginData> data = autoLoginDataRepository.findById(id);
		if (data.isPresent() && data.get().getToken().equals(token)) {
			AutoLoginData currentLoginData = data.get();
			if (data.get().getExpirationTime() > System.currentTimeMillis()) {
				//found data and it does not expire.

				// update token time whenever user uses it;
				currentLoginData.setExpirationTime(System.currentTimeMillis() + (7*24*60*60*1000));
				autoLoginDataRepository.save(currentLoginData);
				return true;	
			} else {
				// expired data is deleted
				autoLoginDataRepository.deleteById(currentLoginData.getId());
				return false;
			}
		}
		
		return false;
	}
}
