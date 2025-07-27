package com.quizserver.controller;

import com.quizserver.dto.SubmitTestDTO;
import com.quizserver.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserTestController {
    
    @Autowired
    private TestService testService;
    
    @GetMapping("/tests")
    public ResponseEntity<?> getAllAvailableTests() {
        try {
            return new ResponseEntity<>(testService.getAllTests(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/test/{id}")
    public ResponseEntity<?> getTestForTaking(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(testService.getAllQuestionsByTest(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/test/submit")
    public ResponseEntity<?> submitTest(@RequestBody SubmitTestDTO dto) {
        try {
            return new ResponseEntity<>(testService.submitTest(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/results/{userId}")
    public ResponseEntity<?> getUserResults(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(testService.getUserResults(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
