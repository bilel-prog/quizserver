package com.quizserver.dto;

import java.util.List;

public class TestDTO {
    private Long id;
    private String title;
    private String description;
    private Long time;
    private Long timePerQuestion;
    private List<QuestionDTO> questions;

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


    public Long getTimePerQuestion() {
        return timePerQuestion;
    }

    public void setTimePerQuestion(Long timePerQuestion) {
        this.timePerQuestion = timePerQuestion;
    }

}
