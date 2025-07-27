package com.quizserver.dto;

import java.util.List;

public class TestDetailsDTO {
    private TestDTO testDTO;
    private List<QuestionDTO>questions;
    public TestDTO getTestDTO() {
        return testDTO;
    }

    public void setTestDTO(TestDTO testDTO) {
        this.testDTO = testDTO;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
}
