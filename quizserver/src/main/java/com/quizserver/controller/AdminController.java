package com.quizserver.controller;

import com.quizserver.dto.QuestionDTO;
import com.quizserver.dto.TestDTO;
import com.quizserver.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {
    
    @Autowired
    private TestService testService;
    
    @PostMapping("/test")
    public ResponseEntity<?> createTest(@RequestBody TestDTO dto) {
        try {
            return new ResponseEntity<>(testService.createTest(dto), HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/test/question")
    public ResponseEntity<?> addQuestion(@RequestBody QuestionDTO dto) {
        try {
            return new ResponseEntity<>(testService.addQuestion(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/tests")
    public ResponseEntity<?> getAllTests() {
        try {
            return new ResponseEntity<>(testService.getAllTests(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/test/{id}")
    public ResponseEntity<?> getTestById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(testService.getAllQuestionsByTest(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/test/{id}/questions")
    public ResponseEntity<?> getTestQuestions(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(testService.getAllQuestionsByTest(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/test/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable Long id) {
        try {
            testService.deleteTest(id);
            return new ResponseEntity<>("Test deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/question/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        try {
            testService.deleteQuestion(id);
            return new ResponseEntity<>("Question deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/fix-question/{id}")
    public ResponseEntity<?> fixQuestionAnswer(@PathVariable Long id) {
        try {
            testService.fixQuestionCorrectAnswer(id);
            return new ResponseEntity<>("Question answer format fixed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/test-results")
    public ResponseEntity<?> getAllTestResults() {
        try {
            return new ResponseEntity<>(testService.getAllTestResults(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/test/{id}")
    public ResponseEntity<?> updateTest(@PathVariable Long id, @RequestBody TestDTO dto) {
        try {
            dto.setId(id);
            return new ResponseEntity<>(testService.updateTest(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
