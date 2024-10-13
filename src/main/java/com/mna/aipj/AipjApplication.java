package com.mna.aipj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AipjApplication {

	public static void main(String[] args) {
		SpringApplication.run(AipjApplication.class, args);
	}

}
