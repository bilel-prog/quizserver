package com.quizserver.entities;
import com.quizserver.dto.QuestionDTO;
import com.quizserver.enums.QuestionType;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Question {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 1000)
    private String questionText;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType = QuestionType.MULTIPLE_CHOICE_SINGLE;
    
    // Legacy fields for backward compatibility
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    
    // New flexible fields for all question types
    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    private List<String> options;
    
    @ElementCollection
    @CollectionTable(name = "question_correct_answers", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "answer_text")
    private List<String> correctAnswers;
    
    @ElementCollection
    @CollectionTable(name = "question_matching_pairs", joinColumns = @JoinColumn(name = "question_id"))
    private List<MatchingPair> matchingPairs;
    
    @ElementCollection
    @CollectionTable(name = "question_sequence_items", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "item_text")
    private List<String> sequenceItems;
    
    @Column(length = 2000)
    private String answerGuidelines;
    
    private Integer maxPoints = 1;
    private Integer minWordCount;
    private Integer timeLimit;
    
    @ManyToOne
    @JoinColumn(name ="Test_Id")
    private Test test;
    
    @Embeddable
    @Data
    public static class MatchingPair {
        @Column(name = "left_item")
        private String left;
        
        @Column(name = "right_item")
        private String right;
        
        public MatchingPair() {}
        
        public MatchingPair(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }
    
    public QuestionDTO getDto() {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(this.id);
        dto.setQuestionText(this.questionText);
        dto.setQuestionType(this.questionType);
        
        // Legacy fields for backward compatibility
        dto.setOptionA(this.optionA);
        dto.setOptionB(this.optionB);
        dto.setOptionC(this.optionC);
        dto.setOptionD(this.optionD);
        dto.setCorrectAnswer(this.correctAnswer);
        
        // New fields
        dto.setOptions(this.options);
        dto.setCorrectAnswers(this.correctAnswers);
        dto.setMatchingPairs(this.matchingPairs);
        dto.setSequenceItems(this.sequenceItems);
        dto.setAnswerGuidelines(this.answerGuidelines);
        dto.setMaxPoints(this.maxPoints);
        dto.setMinWordCount(this.minWordCount);
        dto.setTimeLimit(this.timeLimit);
        
        dto.setTestId(this.test != null ? this.test.getId() : null);
        return dto;
    }
}
