package com.quizserver.service.test;

import com.quizserver.dto.PageDTO;
import com.quizserver.dto.QuestionDTO;
import com.quizserver.dto.SubmitTestDTO;
import com.quizserver.dto.TestDTO;
import com.quizserver.dto.TestResultDTO;

import java.util.List;

public interface TestService {
    // Pagination support
    PageDTO<TestDTO> getAllTestsPaged(int page, int size);
    PageDTO<QuestionDTO> getQuestionsByTestPaged(Long testId, int page, int size);
    PageDTO<TestResultDTO> getUserResultsPaged(Long userId, int page, int size);
    PageDTO<TestResultDTO> getAllTestResultsPaged(int page, int size);
    
    // User-specific test methods
    PageDTO<TestDTO> getUserTestsPaged(Long userId, int page, int size);
    List<TestDTO> getUserTests(Long userId);
    
    // Test management
    TestDTO createTest(TestDTO dto, Long userId);
    TestDTO createTest(TestDTO dto); // Keep for backward compatibility
    QuestionDTO addQuestion(QuestionDTO dto);
    QuestionDTO updateQuestion(QuestionDTO dto);
    List<TestDTO> getAllTests();
    TestDTO getAllQuestionsByTest(Long id);
    TestResultDTO submitTest(SubmitTestDTO request);
    List<TestResultDTO> getAllTestResults();
    List<TestResultDTO> getAllResultsOfUser(Long userId);
    List<TestResultDTO> getUserResults(Long userId);
    List<TestResultDTO> getTestResults(Long testId); // Get all results for a specific test
    PageDTO<TestResultDTO> getTestResultsPaged(Long testId, int page, int size); // Get paginated results for a specific test
    
    // Delete methods with permission checking
    void deleteTest(Long id);
    void deleteTestAsUser(Long testId, Long userId); // User can only delete their own tests
    void deleteTestAsAdmin(Long testId); // Admin can delete any test
    void deleteQuestion(Long id);
    
    TestDTO updateTest(TestDTO dto);
    void fixQuestionCorrectAnswer(Long questionId);
    
    // Permission checking
    boolean canUserDeleteTest(Long testId, Long userId);
    boolean isTestOwner(Long testId, Long userId);
    
    // Admin user management methods
    List<TestDTO> getTestsByCreatedById(Long userId);
    List<TestResultDTO> getTestResultsByUserId(Long userId);
    
    // Paginated versions for lazy loading
    org.springframework.data.domain.Page<TestDTO> getTestsByCreatedByIdPaged(Long userId, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<TestResultDTO> getTestResultsByUserIdPaged(Long userId, org.springframework.data.domain.Pageable pageable);
    
    // Search functionality
    List<TestDTO> searchTests(String query);
}
