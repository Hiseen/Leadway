package com.leadway.leadway_server.services;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import org.springframework.stereotype.Component;

@Component
public class EncryptionService {

    private byte[] salt = new byte[16];
    private IvParameterSpec iv;
    private SecretKey secretKey;
    private SecretKeyFactory factory;
    private int bcryptIterations = 65536;
    private int keyLength = 128;
    private SecureRandom secureRNG;
    private Cipher aesCipher4Encrypt;
    private Cipher aesCipher4Decrypt;

    public EncryptionService() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        // generating PBKDF2 salts and factory
        secureRNG = new SecureRandom();
        secureRNG.nextBytes(salt);
        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        // generating AES related credentials
        KeyGenerator keyGen=KeyGenerator.getInstance("AES");
        keyGen.init(128);
        secretKey = keyGen.generateKey();
        byte[] ivbytes = new byte[16];
        secureRNG.nextBytes(ivbytes);
        iv=new IvParameterSpec(ivbytes);
        aesCipher4Encrypt = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        aesCipher4Encrypt.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        aesCipher4Decrypt = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        aesCipher4Decrypt.init(Cipher.DECRYPT_MODE, secretKey, iv);
    }

    public String PBKDF2Encrypt(String toBeEncrypted) throws InvalidKeySpecException {
        // conduct PBKDF2 encryption that uses `salt`
        KeySpec spec = new PBEKeySpec(toBeEncrypted.toCharArray(), salt, bcryptIterations, keyLength);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Hex.encodeHexString(hash);
    }

    public String AESDecrypt(String encrypted) throws DecoderException, BadPaddingException, IllegalBlockSizeException {
        return new String(aesCipher4Decrypt.doFinal(Hex.decodeHex(encrypted.toCharArray())));
    }

    public String AESEncrypt(String string) throws BadPaddingException, IllegalBlockSizeException {
        return Hex.encodeHexString(aesCipher4Encrypt.doFinal(string.getBytes()));
    }
}
