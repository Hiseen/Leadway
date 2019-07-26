package com.leadway.leadway_server.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import org.springframework.stereotype.Component;

@Component
public class EncryptionService {

    private byte[] salt = new byte[16];
    private SecretKeyFactory factory;
    private int bcryptIterations = 65536;
    private int keyLength = 128;
    private SecureRandom secureRNG;
    private Cipher aesCipher4Encrypt;
    private Cipher aesCipher4Decrypt;

    private static final String aeskey = "aesEncryptionKey";
    private static final String aesinitVector = "encryptionIntVec";

    public EncryptionService() throws NoSuchAlgorithmException {
        // generating PBKDF2 salts and factory
        secureRNG = new SecureRandom();
        secureRNG.nextBytes(salt);
        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    }

    public String PBKDF2Encrypt(String toBeEncrypted) throws InvalidKeySpecException {
        // conduct PBKDF2 encryption that uses `salt`
        KeySpec spec = new PBEKeySpec(toBeEncrypted.toCharArray(), salt, bcryptIterations, keyLength);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Hex.encodeHexString(hash);
    }

    public String AESEncrypt(String word) throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, 
    		InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
    	
        IvParameterSpec iv = new IvParameterSpec(aesinitVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(aeskey.getBytes("UTF-8"), "AES");
        aesCipher4Encrypt = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        aesCipher4Encrypt.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
                
        return Hex.encodeHexString(aesCipher4Encrypt.doFinal(word.getBytes()));
    }

    public String AESDecrypt(String encrypted) throws DecoderException, BadPaddingException,
    		IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, 
    		NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    	
        IvParameterSpec iv = new IvParameterSpec(aesinitVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(aeskey.getBytes("UTF-8"), "AES");
        aesCipher4Decrypt = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        aesCipher4Decrypt.init(Cipher.DECRYPT_MODE, skeySpec, iv);
    	    	
        return new String(aesCipher4Decrypt.doFinal(Hex.decodeHex(encrypted.toCharArray())));

    }
}
