package com.quizserver.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TestResultDTO {
    private Long id;
    private int totalQuestions;
    private int correctAnswers;
    private double percentage;
    private String testName;
    private String testTitle; // Add for user details display
    private String userName;
    private LocalDateTime dateTaken;
    private LocalDateTime takenDate; // Add for user details display
    private double score; // Add for percentage display
}

