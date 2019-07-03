package com.leadway.leadway_server;

import com.leadway.leadway_server.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.MessagingException;

@SpringBootApplication
public class LeadwayWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeadwayWebApplication.class, args);
	}
}
