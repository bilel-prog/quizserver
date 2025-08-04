package com.quizserver.dto;

import java.util.List;

public class TestDTO {
    private Long id;
    private String title;
    private String description;
    private Long time;
    private Long timePerQuestion;
    private List<QuestionDTO> questions;
    private Integer questionCount; // Add this field for caching the count
    private Integer resultCount; // Add this field for caching the result count
    
    // Add creator information
    private Long createdByUserId;
    private String createdByUserName;

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getTime() {
        return time;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public Long getTimePerQuestion() {
        return timePerQuestion;
    }

    public void setTimePerQuestion(Long timePerQuestion) {
        this.timePerQuestion = timePerQuestion;
    }

    // Computed property for question count
    public int getQuestionCount() {
        // If questionCount is cached, use it; otherwise count from questions list
        if (questionCount != null) {
            return questionCount;
        }
        return questions != null ? questions.size() : 0;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }
    
    public int getResultCount() {
        return resultCount != null ? resultCount : 0;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }
}
