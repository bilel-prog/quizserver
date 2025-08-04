package com.quizserver.repository;

import com.quizserver.entities.Test;
import com.quizserver.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test,Long> {
    // Spring Data JPA will provide this automatically
    
    // Find tests created by a specific user
    List<Test> findByCreatedBy(User user);
    Page<Test> findByCreatedBy(User user, Pageable pageable);
    
    // Find tests created by a specific user ID
    List<Test> findByCreatedById(Long userId);
    Page<Test> findByCreatedById(Long userId, Pageable pageable);
}
