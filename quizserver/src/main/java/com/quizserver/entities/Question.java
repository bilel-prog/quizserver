package com.quizserver.entities;
import com.quizserver.dto.QuestionDTO;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String  questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    @ManyToOne
    @JoinColumn(name ="Test_Id")
    private Test test;
    public QuestionDTO getDto() {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(this.id);
        dto.setQuestionText(this.questionText);
        dto.setOptionA(this.optionA);
        dto.setOptionB(this.optionB);
        dto.setOptionC(this.optionC);
        dto.setOptionD(this.optionD);
        dto.setCorrectAnswer(this.correctAnswer);
        dto.setTestId(this.test != null ? this.test.getId() : null);
        return dto;
    }




}
