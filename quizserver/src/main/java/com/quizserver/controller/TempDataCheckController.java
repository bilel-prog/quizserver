package com.quizserver.controller;

import com.quizserver.repository.TestRepository;
import com.quizserver.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/temp")
public class TempDataCheckController {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/check-data")
    public ResponseEntity<?> checkLoadedData() {
        Map<String, Object> data = new HashMap<>();
        
        long testCount = testRepository.count();
        long questionCount = questionRepository.count();
        
        data.put("totalTests", testCount);
        data.put("totalQuestions", questionCount);
        data.put("dataLoaded", testCount > 0);
        data.put("message", testCount > 0 ? 
            "Test data successfully loaded!" : 
            "No test data found - initialization may not have run");
            
        if (testCount > 0) {
            // Get test titles for verification
            var tests = testRepository.findAll();
            var testTitles = tests.stream()
                .map(test -> test.getTitle())
                .toList();
            data.put("testTitles", testTitles);
        }
        
        return ResponseEntity.ok(data);
    }
}
