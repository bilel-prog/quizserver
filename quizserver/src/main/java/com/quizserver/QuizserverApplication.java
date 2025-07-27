package com.quizserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class QuizserverApplication {

	public static void main(String[] args) {
		System.out.println("=== Starting Quiz Beta Application ===");
		System.out.println("Database: quiz_beta_db");
		System.out.println("Port: 8080");
		System.out.println("=======================================");
		
		SpringApplication.run(QuizserverApplication.class, args);
		
		System.out.println("=== Quiz Beta Application Started Successfully ===");
	}
}
