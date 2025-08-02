package com.quizserver.controller;

import com.quizserver.dto.SubmitTestDTO;
import com.quizserver.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserTestController {
    
    @Autowired
    private TestService testService;
    
    @GetMapping("/tests")
    public ResponseEntity<?> getAllAvailableTests(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size) {
        try {
            return new ResponseEntity<>(testService.getAllTestsPaged(page, size), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/test/{id}")
    public ResponseEntity<?> getTestForTaking(@PathVariable Long id,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size) {
        System.out.println("[UserTestController] Requested test ID: " + id);
        try {
            Object response = testService.getQuestionsByTestPaged(id, page, size);
            System.out.println("[UserTestController] Response for test ID " + id + ": " + response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("[UserTestController] Error for test ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/test/{id}/full")
    public ResponseEntity<?> getFullTestForTaking(@PathVariable Long id) {
        System.out.println("[UserTestController] Requested full test ID: " + id);
        try {
            Object response = testService.getAllQuestionsByTest(id);
            System.out.println("[UserTestController] Full test response for ID " + id + ": " + response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("[UserTestController] Error for full test ID " + id + ": " + e.getMessage());
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
    public ResponseEntity<?> getUserResults(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size) {
        try {
            return new ResponseEntity<>(testService.getUserResultsPaged(userId, page, size), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
