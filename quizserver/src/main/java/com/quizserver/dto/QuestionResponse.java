package com.quizserver.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class QuestionResponse {
    private Long questionId;
    
    // Legacy field for backward compatibility
    private String selectedOption;
    
    // New flexible fields for all question types
    private List<String> selectedAnswers;      // For multiple choice questions
    private String textAnswer;                 // For fill-in-blank, short answer, essay
    private Map<String, String> matchingAnswers; // For matching questions (left -> right mapping)
    private List<String> sequenceAnswers;      // For sequencing questions (ordered list)
    
    // Constructors
    public QuestionResponse() {}
    
    public QuestionResponse(Long questionId, String selectedOption) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
    }
    
    // Helper methods for backward compatibility
    public String getSelectedOption() {
        if (selectedOption != null) {
            return selectedOption;
        }
        if (selectedAnswers != null && !selectedAnswers.isEmpty()) {
            return selectedAnswers.get(0);
        }
        return null;
    }
    
    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
        if (selectedOption != null && (selectedAnswers == null || selectedAnswers.isEmpty())) {
            this.selectedAnswers = List.of(selectedOption);
        }
    }
}
