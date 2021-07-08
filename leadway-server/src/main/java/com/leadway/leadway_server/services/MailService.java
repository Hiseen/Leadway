package com.leadway.leadway_server.services;


import org.springframework.stereotype.Component;

import com.leadway.leadway_server.constants.LeadwayConstants;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class MailService {

    private Session mailSession;
    private final String hostname = "http://localhost:8080";
    private final String senderEmail = "121989255@qq.com";
    private final String authToken = "uupakcvaukdtbjdb";

    private MailService() {
        Properties mailServerProperties;
        mailServerProperties = System.getProperties();
        mailServerProperties.setProperty("mail.smtp.host", "smtp.qq.com");
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        mailSession = Session.getDefaultInstance(mailServerProperties,
                new Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, authToken);
                    }});
    }

    void sendVerificationMailTo(String email, String verificationCode)throws MessagingException {
    	if (LeadwayConstants.isDeveloping) {
    		// if it is in development mode, dont send the email.
    		return;
    	}
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(senderEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Leadway Email Verification");
        
        String verificationURL = hostname + "/api/verify?code=" + verificationCode;
        String verificationContent = "Welcome to Leadway!<br><br>" +
                "Thank you for signing up. Please verify your e-mail address by visiting the link below in your browser of choice:<br>" +
                verificationURL;
        
        message.setContent(verificationContent, "text/html" );
        Transport.send(message);
    }

}
