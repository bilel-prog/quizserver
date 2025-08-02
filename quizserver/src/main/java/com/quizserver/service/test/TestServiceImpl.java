package com.quizserver.service.test;

import com.quizserver.dto.*;
import com.quizserver.entities.Question;
import com.quizserver.entities.Test;
import com.quizserver.entities.TestResult;
import com.quizserver.entities.User;
import com.quizserver.repository.QuestionRepository;
import com.quizserver.repository.TestRepository;
import com.quizserver.repository.TestResultRepository;
import com.quizserver.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public QuestionDTO addQuestion(QuestionDTO dto) {
        Test test = testRepository.findById(dto.getTestId())
                .orElseThrow(() -> new RuntimeException("Test not found with ID " + dto.getTestId()));

        Question q = new Question();
        q.setQuestionText(dto.getQuestionText());
        q.setOptionA(dto.getOptionA());
        q.setOptionB(dto.getOptionB());
        q.setOptionC(dto.getOptionC());
        q.setOptionD(dto.getOptionD());
        q.setCorrectAnswer(dto.getCorrectAnswer());
        q.setTest(test);

        questionRepository.save(q);

        // ✅ Mise à jour de test.time avec test.timePerQuestion
        if (test.getTimePerQuestion() == null) {
            throw new RuntimeException("Time per question not set for this test.");
        }



        test.setTime(test.getTime() + test.getTimePerQuestion());
        testRepository.save(test);

        return q.getDto(); // Assure-toi que Question.java a bien une méthode getDto()
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
        Test test=testRepository.findById(request.getTestId()).orElseThrow(()->new EntityNotFoundException("test not found"));
        User user=userRepository.findById(request.getUserId()).orElseThrow(()->new EntityNotFoundException("user not found"));
        int correctAnswers=0;
        for (QuestionResponse response:request.getResponses()){
            Question question=questionRepository.findById(response.getQuestionId()).orElseThrow(()->new EntityNotFoundException("question not found"));
            if (question.getCorrectAnswer().equals(response.getSelectedOption())){
                correctAnswers++;
            }
        }
        int totalQuestions=test.getQuestions().size();
        double percentage=((double) correctAnswers/totalQuestions)*100;
        TestResult testResult=new TestResult();
        testResult.setTest(test);
        testResult.setUser(user);
        testResult.setTotalQuestions(totalQuestions);
        testResult.setCorrectAnswers(correctAnswers);
        testResult.setPercentage(percentage);
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
}
