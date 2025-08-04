package com.quizserver.dto;

import com.quizserver.enums.QuestionType;
import com.quizserver.entities.Question.MatchingPair;
import lombok.Data;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String questionText;
    private QuestionType questionType = QuestionType.MULTIPLE_CHOICE_SINGLE;
    
    // Legacy fields for backward compatibility
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    
    // New flexible fields for all question types
    private List<String> options;
    private List<String> correctAnswers;
    private List<MatchingPair> matchingPairs;
    private List<String> sequenceItems;
    private String answerGuidelines;
    private Integer maxPoints = 1;
    private Integer minWordCount;
    private Integer timeLimit;
    
    private Long testId;

    // Legacy getters and setters for backward compatibility
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public String getOptionA() {
        return optionA;
    }
    
    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }
    
    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }
    
    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }
    
    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Long getTestId() {
        return testId;
    }
    
    public void setTestId(Long testId) {
        this.testId = testId;
    }

    // New getters and setters
    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public List<MatchingPair> getMatchingPairs() {
        return matchingPairs;
    }

    public void setMatchingPairs(List<MatchingPair> matchingPairs) {
        this.matchingPairs = matchingPairs;
    }

    public List<String> getSequenceItems() {
        return sequenceItems;
    }

    public void setSequenceItems(List<String> sequenceItems) {
        this.sequenceItems = sequenceItems;
    }

    public String getAnswerGuidelines() {
        return answerGuidelines;
    }

    public void setAnswerGuidelines(String answerGuidelines) {
        this.answerGuidelines = answerGuidelines;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Integer getMinWordCount() {
        return minWordCount;
    }

    public void setMinWordCount(Integer minWordCount) {
        this.minWordCount = minWordCount;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
}
