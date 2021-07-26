package com.g18;

import com.g18.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.mail.MessagingException;

@SpringBootApplication
@EnableAsync
public class SlaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SlaServerApplication.class, args);
	}

}
