package com.quizserver.service.test;

import com.quizserver.dto.*;
import com.quizserver.entities.Question;
import com.quizserver.entities.Test;
import com.quizserver.entities.TestResult;
import com.quizserver.entities.User;
import com.quizserver.enums.QuestionType;
import com.quizserver.repository.QuestionRepository;
import com.quizserver.repository.TestRepository;
import com.quizserver.repository.TestResultRepository;
import com.quizserver.repository.UserRepository;
import com.quizserver.service.grading.QuestionGradingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public PageDTO<TestDTO> getAllTestsPaged(int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Test> pageResult = testRepository.findAll(pageable);
        List<TestDTO> content = pageResult.getContent().stream()
                .map(test -> {
                    TestDTO dto = test.getDto();
                    // Manually set the question count for performance
                    long questionCount = questionRepository.countByTestId(test.getId());
                    dto.setQuestionCount((int) questionCount);
                    return dto;
                })
                .collect(Collectors.toList());
        
        return new PageDTO<>(
            content,
            pageResult.getTotalPages(),
            pageResult.getTotalElements(),
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.isFirst(),
            pageResult.isLast()
        );
    }

    @Override
    public PageDTO<QuestionDTO> getQuestionsByTestPaged(Long testId, int page, int size) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new EntityNotFoundException("Test not found with ID " + testId));
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Question> pageResult = questionRepository.findByTest(test, pageable);
        List<QuestionDTO> content = pageResult.getContent().stream()
                .map(Question::getDto)
                .collect(Collectors.toList());
        
        return new PageDTO<>(
            content,
            pageResult.getTotalPages(),
            pageResult.getTotalElements(),
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.isFirst(),
            pageResult.isLast()
        );
    }

    @Override
    public PageDTO<TestResultDTO> getUserResultsPaged(Long userId, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<TestResult> pageResult = testResultRepository.findAllByUserId(userId, pageable);
        List<TestResultDTO> content = pageResult.getContent().stream()
                .map(TestResult::getDTO)
                .collect(Collectors.toList());
        
        return new PageDTO<>(
            content,
            pageResult.getTotalPages(),
            pageResult.getTotalElements(),
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.isFirst(),
            pageResult.isLast()
        );
    }

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionGradingService questionGradingService;
    @Override
    public TestDTO createTest(TestDTO dto) {
        if (dto.getTimePerQuestion() == null || dto.getTimePerQuestion() <= 0) {
            throw new IllegalArgumentException("Time per question must be a positive number.");
        }

        Test test = new Test();
        test.setTitle(dto.getTitle());
        test.setDescription(dto.getDescription());
        test.setTimePerQuestion(dto.getTimePerQuestion());

        // Don't set test.setTime() here — it should be computed later when questions are added
        test.setTime(0L);
        return testRepository.save(test).getDto();
    }

    @Override
    public TestDTO createTest(TestDTO dto, Long userId) {
        if (dto.getTimePerQuestion() == null || dto.getTimePerQuestion() <= 0) {
            throw new IllegalArgumentException("Time per question must be a positive number.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID " + userId));

        Test test = new Test();
        test.setTitle(dto.getTitle());
        test.setDescription(dto.getDescription());
        test.setTimePerQuestion(dto.getTimePerQuestion());
        test.setCreatedBy(user); // Set the creator
        test.setTime(0L);
        
        return testRepository.save(test).getDto();
    }

    @Override
    public PageDTO<TestDTO> getUserTestsPaged(Long userId, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Test> pageResult = testRepository.findByCreatedById(userId, pageable);
        List<TestDTO> content = pageResult.getContent().stream()
                .map(test -> {
                    TestDTO dto = test.getDto();
                    long questionCount = questionRepository.countByTestId(test.getId());
                    dto.setQuestionCount((int) questionCount);
                    return dto;
                })
                .collect(Collectors.toList());
        
        return new PageDTO<>(
            content,
            pageResult.getTotalPages(),
            pageResult.getTotalElements(),
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.isFirst(),
            pageResult.isLast()
        );
    }

    @Override
    public List<TestDTO> getUserTests(Long userId) {
        return testRepository.findByCreatedById(userId).stream()
                .map(test -> {
                    TestDTO dto = test.getDto();
                    long questionCount = questionRepository.countByTestId(test.getId());
                    dto.setQuestionCount((int) questionCount);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTestAsUser(Long testId, Long userId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new EntityNotFoundException("Test not found with ID " + testId));
        
        // Allow deletion if:
        // 1. User is the owner, OR
        // 2. Test has no owner (legacy tests from before ownership was implemented)
        if (test.getCreatedBy() != null && !test.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("You can only delete tests that you created");
        }
        
        testRepository.delete(test);
    }

    @Override
    public void deleteTestAsAdmin(Long testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new EntityNotFoundException("Test not found with ID " + testId));
        testRepository.delete(test);
    }

    @Override
    public boolean canUserDeleteTest(Long testId, Long userId) {
        return isTestOwner(testId, userId);
    }

    @Override
    public boolean isTestOwner(Long testId, Long userId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new EntityNotFoundException("Test not found with ID " + testId));
        return test.getCreatedBy() != null && test.getCreatedBy().getId().equals(userId);
    }



    @Override
    public QuestionDTO addQuestion(QuestionDTO dto) {
        Test test = testRepository.findById(dto.getTestId())
                .orElseThrow(() -> new RuntimeException("Test not found with ID " + dto.getTestId()));

        Question q = new Question();
        q.setQuestionText(dto.getQuestionText());
        q.setQuestionType(dto.getQuestionType() != null ? dto.getQuestionType() : QuestionType.MULTIPLE_CHOICE_SINGLE);
        
        // Set legacy fields for backward compatibility
        q.setOptionA(dto.getOptionA());
        q.setOptionB(dto.getOptionB());
        q.setOptionC(dto.getOptionC());
        q.setOptionD(dto.getOptionD());
        q.setCorrectAnswer(dto.getCorrectAnswer());
        
        // Set new flexible fields
        q.setOptions(dto.getOptions());
        q.setCorrectAnswers(dto.getCorrectAnswers());
        q.setMatchingPairs(dto.getMatchingPairs());
        q.setSequenceItems(dto.getSequenceItems());
        q.setAnswerGuidelines(dto.getAnswerGuidelines());
        q.setMaxPoints(dto.getMaxPoints() != null ? dto.getMaxPoints() : 1);
        q.setMinWordCount(dto.getMinWordCount());
        q.setTimeLimit(dto.getTimeLimit());
        
        q.setTest(test);
        questionRepository.save(q);

        // Update test time with timePerQuestion
        if (test.getTimePerQuestion() == null) {
            throw new RuntimeException("Time per question not set for this test.");
        }

        test.setTime(test.getTime() + test.getTimePerQuestion());
        testRepository.save(test);

        return q.getDto();
    }

    @Override
    public QuestionDTO updateQuestion(QuestionDTO dto) {
        Question existingQuestion = questionRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Question not found with ID " + dto.getId()));

        existingQuestion.setQuestionText(dto.getQuestionText());
        existingQuestion.setQuestionType(dto.getQuestionType() != null ? dto.getQuestionType() : existingQuestion.getQuestionType());
        
        // Update legacy fields for backward compatibility
        existingQuestion.setOptionA(dto.getOptionA());
        existingQuestion.setOptionB(dto.getOptionB());
        existingQuestion.setOptionC(dto.getOptionC());
        existingQuestion.setOptionD(dto.getOptionD());
        existingQuestion.setCorrectAnswer(dto.getCorrectAnswer());
        
        // Update new flexible fields
        existingQuestion.setOptions(dto.getOptions());
        existingQuestion.setCorrectAnswers(dto.getCorrectAnswers());
        existingQuestion.setMatchingPairs(dto.getMatchingPairs());
        existingQuestion.setSequenceItems(dto.getSequenceItems());
        existingQuestion.setAnswerGuidelines(dto.getAnswerGuidelines());
        existingQuestion.setMaxPoints(dto.getMaxPoints() != null ? dto.getMaxPoints() : existingQuestion.getMaxPoints());
        existingQuestion.setMinWordCount(dto.getMinWordCount());
        existingQuestion.setTimeLimit(dto.getTimeLimit());

        questionRepository.save(existingQuestion);
        return existingQuestion.getDto();
    }
    public List<TestDTO> getAllTests() {
        return testRepository.findAll().stream()
                .map(test -> {
                    TestDTO dto = test.getDto();
                    // Set the question count for each test
                    long questionCount = questionRepository.countByTestId(test.getId());
                    dto.setQuestionCount((int) questionCount);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public TestDTO getAllQuestionsByTest(Long id){
        Optional<Test>optionalTest=testRepository.findById(id);
        TestDetailsDTO testDetailsDTO=new TestDetailsDTO();
        if (optionalTest.isPresent()){
            TestDTO testDTO=optionalTest.get().getDto();
            testDetailsDTO.setTestDTO(testDTO);
            return testDetailsDTO.getTestDTO();
        }
        return testDetailsDTO.getTestDTO();
    }
    public TestResultDTO submitTest(SubmitTestDTO request){
        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new EntityNotFoundException("Test not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        int correctAnswers = 0;
        int totalPoints = 0;
        int earnedPoints = 0;
        boolean requiresManualGrading = false;
        
        for (QuestionResponse response : request.getResponses()) {
            Question question = questionRepository.findById(response.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Question not found"));
            
            // Calculate total possible points
            totalPoints += question.getMaxPoints();
            
            // Grade the question using the new grading service
            QuestionGradingService.QuestionGradingResult result = 
                questionGradingService.gradeQuestion(question, response);
            
            if (result.isCorrect() && !result.requiresManualGrading()) {
                correctAnswers++;
            }
            
            earnedPoints += result.getPoints();
            
            if (result.requiresManualGrading()) {
                requiresManualGrading = true;
            }
        }
        
        int totalQuestions = test.getQuestions().size();
        double percentage = totalQuestions > 0 ? ((double) correctAnswers / totalQuestions) * 100 : 0;
        double pointsPercentage = totalPoints > 0 ? ((double) earnedPoints / totalPoints) * 100 : 0;
        
        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setUser(user);
        testResult.setTotalQuestions(totalQuestions);
        testResult.setCorrectAnswers(correctAnswers);
        testResult.setPercentage(percentage);
        testResult.setDateTaken(LocalDateTime.now());
        
        // Add fields for new grading system if they exist in TestResult entity
        // testResult.setTotalPoints(totalPoints);
        // testResult.setEarnedPoints(earnedPoints);
        // testResult.setPointsPercentage(pointsPercentage);
        // testResult.setRequiresManualGrading(requiresManualGrading);
        
        return testResultRepository.save(testResult).getDTO();
    }
    
    public List<TestResultDTO> getAllTestResults() {
        return testResultRepository.findAll().stream().map(TestResult::getDTO).collect(Collectors.toList());
    }
    
    @Override
    public PageDTO<TestResultDTO> getAllTestResultsPaged(int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<TestResult> pageResult = testResultRepository.findAll(pageable);
        List<TestResultDTO> content = pageResult.getContent().stream()
                .map(TestResult::getDTO)
                .collect(Collectors.toList());
        
        return new PageDTO<>(
            content,
            pageResult.getTotalPages(),
            pageResult.getTotalElements(),
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.isFirst(),
            pageResult.isLast()
        );
    }

    public List<TestResultDTO> getAllResultsOfUser(Long userId) {
        return testResultRepository.findAllByUserId(userId).stream().map(TestResult::getDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<TestResultDTO> getUserResults(Long userId) {
        return getAllResultsOfUser(userId);
    }
    
    @Override
    public List<TestResultDTO> getTestResults(Long testId) {
        return testResultRepository.findAllByTestId(testId).stream().map(TestResult::getDTO).collect(Collectors.toList());
    }
    
    @Override
    public PageDTO<TestResultDTO> getTestResultsPaged(Long testId, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<TestResult> pageResult = testResultRepository.findAllByTestId(testId, pageable);
        List<TestResultDTO> content = pageResult.getContent().stream()
                .map(TestResult::getDTO)
                .collect(Collectors.toList());
        
        return new PageDTO<>(
            content,
            pageResult.getTotalPages(),
            pageResult.getTotalElements(),
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.isFirst(),
            pageResult.isLast()
        );
    }
    
    @Override
    public void deleteTest(Long id) {
        Test test = testRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Test not found with id: " + id));
        testRepository.delete(test);
    }
    
    @Override
    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + id));
        questionRepository.delete(question);
    }
    
    @Override
    public TestDTO updateTest(TestDTO dto) {
        Test test = testRepository.findById(dto.getId())
            .orElseThrow(() -> new EntityNotFoundException("Test not found with id: " + dto.getId()));
        
        test.setTitle(dto.getTitle());
        test.setDescription(dto.getDescription());
        test.setTimePerQuestion(dto.getTimePerQuestion());
        
        if (dto.getTime() != null) {
            test.setTime(dto.getTime());
        }
        
        return testRepository.save(test).getDto();
    }
    
    @Override
    public void fixQuestionCorrectAnswer(Long questionId) {
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + questionId));
        
        String currentAnswer = question.getCorrectAnswer();
        
        // Check if the answer is in text format and convert to letter format
        if (currentAnswer.equals(question.getOptionA())) {
            question.setCorrectAnswer("A");
        } else if (currentAnswer.equals(question.getOptionB())) {
            question.setCorrectAnswer("B");
        } else if (currentAnswer.equals(question.getOptionC())) {
            question.setCorrectAnswer("C");
        } else if (currentAnswer.equals(question.getOptionD())) {
            question.setCorrectAnswer("D");
        }
        // If it's already in letter format (A, B, C, D), no change needed
        
        questionRepository.save(question);
    }
    
    @Override
    public List<TestDTO> getTestsByCreatedById(Long userId) {
        List<Test> tests = testRepository.findByCreatedById(userId);
        return tests.stream().map(test -> {
            TestDTO dto = test.getDto();
            // Add question count and result count
            long questionCount = questionRepository.countByTestId(test.getId());
            long resultCount = testResultRepository.findAllByTestId(test.getId()).size();
            dto.setQuestionCount((int) questionCount);
            dto.setResultCount((int) resultCount);
            return dto;
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<TestResultDTO> getTestResultsByUserId(Long userId) {
        List<TestResult> results = testResultRepository.findAllByUserId(userId);
        return results.stream().map(result -> {
            TestResultDTO dto = result.getDTO(); // Use correct method name
            // Add test title for better display
            dto.setTestTitle(result.getTest().getTitle());
            dto.setTakenDate(result.getDateTaken()); // Use correct field name
            dto.setScore(result.getPercentage()); // Set score as percentage
            return dto;
        }).collect(Collectors.toList());
    }
    
    @Override
    public org.springframework.data.domain.Page<TestDTO> getTestsByCreatedByIdPaged(Long userId, org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.domain.Page<Test> testPage = testRepository.findByCreatedById(userId, pageable);
        return testPage.map(test -> {
            TestDTO dto = test.getDto();
            // Add question count and result count
            long questionCount = questionRepository.countByTestId(test.getId());
            long resultCount = testResultRepository.findAllByTestId(test.getId()).size();
            dto.setQuestionCount((int) questionCount);
            dto.setResultCount((int) resultCount);
            return dto;
        });
    }
    
    @Override
    public org.springframework.data.domain.Page<TestResultDTO> getTestResultsByUserIdPaged(Long userId, org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.domain.Page<TestResult> resultPage = testResultRepository.findAllByUserId(userId, pageable);
        return resultPage.map(result -> {
            TestResultDTO dto = result.getDTO();
            // Add test title for better display
            dto.setTestTitle(result.getTest().getTitle());
            dto.setTakenDate(result.getDateTaken());
            dto.setScore(result.getPercentage());
            return dto;
        });
    }
    
    @Override
    public List<TestDTO> searchTests(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllTests();
        }
        
        // Convert query to lowercase for case-insensitive search
        String searchTerm = query.toLowerCase().trim();
        
        // Search by title and description
        List<Test> matchingTests = testRepository.findAll().stream()
            .filter(test -> {
                boolean titleMatch = test.getTitle() != null && 
                                  test.getTitle().toLowerCase().contains(searchTerm);
                boolean descriptionMatch = test.getDescription() != null && 
                                         test.getDescription().toLowerCase().contains(searchTerm);
                return titleMatch || descriptionMatch;
            })
            .collect(Collectors.toList());
        
        return matchingTests.stream()
            .map(test -> {
                // Create a simple TestDTO without questions for search results
                TestDTO dto = new TestDTO();
                dto.setId(test.getId());
                dto.setTitle(test.getTitle());
                dto.setDescription(test.getDescription());
                dto.setTime(test.getTime());
                
                // Add creator information if available
                if (test.getCreatedBy() != null) {
                    dto.setCreatedByUserId(test.getCreatedBy().getId());
                    dto.setCreatedByUserName(test.getCreatedBy().getName());
                }
                
                // Add question count for display
                long questionCount = questionRepository.countByTestId(test.getId());
                dto.setQuestionCount((int) questionCount);
                
                // Don't include questions array to keep response lightweight
                dto.setQuestions(null);
                
                return dto;
            })
            .collect(Collectors.toList());
    }
}
