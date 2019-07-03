package com.leadway.leadway_server.services;


import org.springframework.stereotype.Component;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class MailService {

    private Session mailSession;
    private String hostname = "http://localhost:8080";
    private String senderEmail = "121989255@qq.com";
    private String authToken = "";

    private MailService() {
        Properties mailServerProperties;
        mailServerProperties = System.getProperties();
        mailServerProperties.setProperty("mail.smtp.host", "smtp.qq.com");
        mailServerProperties.put("mail.smtp.port", "465");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        mailServerProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailSession = Session.getDefaultInstance(mailServerProperties,
                new Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, authToken);
                    }});
    }

    void sendVerificationMailTo(String email, long verificationCode)throws MessagingException {
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(senderEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Leadway Email Verification");
        String verificationURL=hostname+"/verify?code="+verificationCode;
        message.setContent("Welcome to Leadway!<br><br>" +
                        "Thank you for signing up. Please verify your e-mail address by visiting the link below in your browser of choice:<br>" +
                        verificationURL+"",
                "text/html" );
        Transport.send(message);
    }

}
