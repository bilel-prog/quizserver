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
    TestDTO createTest(TestDTO dto);
    QuestionDTO addQuestion(QuestionDTO dto);
    List<TestDTO> getAllTests();
    TestDTO getAllQuestionsByTest(Long id);
    TestResultDTO submitTest(SubmitTestDTO request);
    List<TestResultDTO> getAllTestResults();
    List<TestResultDTO> getAllResultsOfUser(Long userId);
    List<TestResultDTO> getUserResults(Long userId);
    void deleteTest(Long id);
    void deleteQuestion(Long id);
    TestDTO updateTest(TestDTO dto);
    void fixQuestionCorrectAnswer(Long questionId);
}
