package com.quizserver.controller;

import com.quizserver.dto.QuestionDTO;
import com.quizserver.dto.SubmitTestDTO;
import com.quizserver.dto.TestDTO;
import com.quizserver.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/test")
public class TestController {
    @Autowired
    private TestService testService;
    @PostMapping()
    public ResponseEntity<?>createTest(@RequestBody TestDTO dto){
        try {
            return new ResponseEntity<>(testService.createTest(dto), HttpStatus.OK);
        }catch (Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);}
    }
    @PostMapping("/question")
    public ResponseEntity<?> addQuestion(@RequestBody QuestionDTO dto) {
        try {
            return new ResponseEntity<>(testService.addQuestion(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping()
    public ResponseEntity<?> getAllTests() {
        try {
            return new ResponseEntity<>(testService.getAllTests(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getAllQuestions(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(testService.getAllQuestionsByTest(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/submit-test")
    public ResponseEntity<?>submitTest(@RequestBody SubmitTestDTO dto){
        try {
            return new ResponseEntity<>(testService.submitTest(dto), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);}
    }
    @GetMapping("/test-result")
    public ResponseEntity<?>getAllTestResults(){
        try {
            return new ResponseEntity<>(testService.getAllTestResults(), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);}
    }
    @GetMapping("/test-results/{id}")
    public ResponseEntity<?>getAllTestResultsOfUser(@PathVariable Long id){
        try {
            return new ResponseEntity<>(testService.getAllResultsOfUser(id), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);}
    }
}
