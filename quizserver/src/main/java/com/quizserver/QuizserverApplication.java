package com.quizserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class QuizserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizserverApplication.class, args);
	}
}
