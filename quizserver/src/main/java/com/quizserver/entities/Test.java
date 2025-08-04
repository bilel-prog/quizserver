package com.quizserver.entities;

import com.quizserver.dto.TestDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private Long timePerQuestion;  // ✅ à ajouter
    private Long time;

    // Add user ownership
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<Question> questions;

    public TestDTO getDto() {
        TestDTO dto = new TestDTO();
        dto.setId(this.id);
        dto.setTitle(this.title);
        dto.setDescription(this.description);
        dto.setTime(this.time);
        dto.setTimePerQuestion(this.timePerQuestion); // ✅ à ajouter
        
        // Add creator information
        if (this.createdBy != null) {
            dto.setCreatedByUserId(this.createdBy.getId());
            dto.setCreatedByUserName(this.createdBy.getName());
        }
        
        if (this.questions != null) {
            dto.setQuestions(
                    this.questions.stream()
                            .map(Question::getDto)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }
}

