package com.InFrame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class InFrameWasApplication {

	public static void main(String[] args) {
		SpringApplication.run(InFrameWasApplication.class, args);
	}

}
