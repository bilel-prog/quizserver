package com.quizserver.controller;

import com.quizserver.dto.QuestionDTO;
import com.quizserver.dto.TestDTO;
import com.quizserver.enums.QuestionType;
import com.quizserver.service.test.TestService;
import com.quizserver.service.user.UserService;
import com.quizserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private TestService testService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/test")
    public ResponseEntity<?> createTest(@RequestBody TestDTO dto, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            return new ResponseEntity<>(testService.createTest(dto, userId), HttpStatus.CREATED);
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
    
    @GetMapping("/question/types")
    public ResponseEntity<?> getQuestionTypes() {
        try {
            return new ResponseEntity<>(QuestionType.values(), HttpStatus.OK);
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
    
    @GetMapping("/tests/paged")
    public ResponseEntity<?> getAllTestsPaged(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        try {
            return new ResponseEntity<>(testService.getAllTestsPaged(page, size), HttpStatus.OK);
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
    
    @GetMapping("/test/{id}/questions/paged")
    public ResponseEntity<?> getTestQuestionsPaged(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        try {
            return new ResponseEntity<>(testService.getQuestionsByTestPaged(id, page, size), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/test/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> deleteTest(@PathVariable Long id) {
        try {
            testService.deleteTestAsAdmin(id);
            
            // Return JSON response instead of plain string
            java.util.Map<String, String> response = new java.util.HashMap<>();
            response.put("message", "Test deleted successfully");
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Return JSON error response
            java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
    
    @GetMapping("/test-results/paged")
    public ResponseEntity<?> getAllTestResultsPaged(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        try {
            return new ResponseEntity<>(testService.getAllTestResultsPaged(page, size), HttpStatus.OK);
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
    
    @PutMapping("/test/question/{questionId}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long questionId, @RequestBody QuestionDTO dto) {
        try {
            dto.setId(questionId);
            return new ResponseEntity<>(testService.updateQuestion(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/test/question/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long questionId) {
        try {
            testService.deleteQuestion(questionId);
            java.util.Map<String, String> response = new java.util.HashMap<>();
            response.put("message", "Question deleted successfully");
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    
    // User Management Endpoints
    @GetMapping("/users/paged")
    public ResponseEntity<?> getUsersPaged(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return new ResponseEntity<>(userService.getAllUsersPaged(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            java.util.Map<String, String> response = new java.util.HashMap<>();
            response.put("message", "User deleted successfully");
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/user/{userId}/details")
    public ResponseEntity<?> getUserDetails(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/user/{userId}/created-tests")
    public ResponseEntity<?> getUserCreatedTests(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return new ResponseEntity<>(testService.getTestsByCreatedByIdPaged(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/user/{userId}/taken-tests")
    public ResponseEntity<?> getUserTakenTests(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return new ResponseEntity<>(testService.getTestResultsByUserIdPaged(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/tests/search")
    public ResponseEntity<?> searchTests(@RequestParam String query) {
        try {
            return new ResponseEntity<>(testService.searchTests(query), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/question/types/stats")
    public ResponseEntity<?> getQuestionTypeStats() {
        try {
            // This would return statistics about question types usage
            // For now, just return the available types with their descriptions
            return new ResponseEntity<>(QuestionType.values(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Helper method to extract user ID from JWT token
    private Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new RuntimeException("Authorization token required");
    }
}
