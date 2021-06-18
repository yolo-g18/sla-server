package com.g18;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SlaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlaServerApplication.class, args);
	}

}
