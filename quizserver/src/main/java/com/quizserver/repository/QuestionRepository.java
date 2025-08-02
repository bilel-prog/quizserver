package com.quizserver.repository;

import com.quizserver.entities.Question;
import com.quizserver.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    org.springframework.data.domain.Page<Question> findByTest(Test test, org.springframework.data.domain.Pageable pageable);
    long countByTest(Test test);
    long countByTestId(Long testId);
}
