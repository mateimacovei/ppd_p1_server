package com.example.ppd_p1_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PpdP1ServerApplication {

	public static void main(String[] args) {

		SpringApplication.run(PpdP1ServerApplication.class, args);
	}

}
