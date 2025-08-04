package com.quizserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizserver.dto.QuestionDTO;
import com.quizserver.dto.SubmitTestDTO;
import com.quizserver.dto.TestDTO;
import com.quizserver.entities.User;
import com.quizserver.enums.QuestionType;
import com.quizserver.repository.UserRepository;
import com.quizserver.service.test.TestService;
import com.quizserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserTestController {
    
    @Autowired
    private TestService testService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    // Get all available tests (for taking tests)
    @GetMapping("/tests")
    public ResponseEntity<?> getAllAvailableTests(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size) {
        try {
            return new ResponseEntity<>(testService.getAllTestsPaged(page, size), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Get user's own tests (tests they created)
    @GetMapping("/my-tests")
    public ResponseEntity<?> getMyTests(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            return new ResponseEntity<>(testService.getUserTestsPaged(userId, page, size), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Create a new test
    @PostMapping("/test")
    public ResponseEntity<?> createTest(@RequestBody TestDTO dto, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            TestDTO createdTest = testService.createTest(dto, userId);
            return new ResponseEntity<>(createdTest, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Delete own test
    @DeleteMapping("/test/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> deleteMyTest(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            System.out.println("[UserTestController] Attempting to delete test " + id + " for user " + userId);
            testService.deleteTestAsUser(id, userId);
            System.out.println("[UserTestController] Successfully deleted test " + id);
            
            // Return JSON response instead of plain string
            java.util.Map<String, String> response = new java.util.HashMap<>();
            response.put("message", "Test deleted successfully");
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("[UserTestController] Error deleting test " + id + ": " + e.getMessage());
            e.printStackTrace();
            
            // Return JSON error response
            java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    
    // Check if user can delete test
    @GetMapping("/test/{id}/can-delete")
    public ResponseEntity<?> canDeleteTest(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            boolean canDelete = testService.canUserDeleteTest(id, userId);
            return new ResponseEntity<>(canDelete, HttpStatus.OK);
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
    
    // Update own test
    @PutMapping("/test/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updateMyTest(@PathVariable Long id, @RequestBody TestDTO dto, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            System.out.println("[UserTestController] Attempting to update test " + id + " for user " + userId);
            
            // Check if user owns the test
            if (!testService.isTestOwner(id, userId)) {
                return new ResponseEntity<>("You can only update your own tests", HttpStatus.FORBIDDEN);
            }
            
            // Set the ID and update
            dto.setId(id);
            TestDTO updatedTest = testService.updateTest(dto);
            System.out.println("[UserTestController] Successfully updated test " + id);
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("message", "Test updated successfully");
            response.put("status", "success");
            response.put("test", updatedTest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("[UserTestController] Error updating test " + id + ": " + e.getMessage());
            e.printStackTrace();
            
            java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
    
    // Get results for a specific test (only for test owners)
    @GetMapping("/my-tests/{testId}/results")
    public ResponseEntity<?> getMyTestResults(@PathVariable Long testId, 
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            System.out.println("[UserTestController] Getting results for test " + testId + " by user " + userId + " (page=" + page + ", size=" + size + ")");
            
            // Check if user owns the test
            if (!testService.isTestOwner(testId, userId)) {
                return new ResponseEntity<>("You can only view results for tests you created", HttpStatus.FORBIDDEN);
            }
            
            return new ResponseEntity<>(testService.getTestResultsPaged(testId, page, size), HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("[UserTestController] Error getting test results: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Add question to own test
    @PostMapping("/test/{testId}/question")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> addQuestionToMyTest(@PathVariable Long testId, @RequestBody Object questionData, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            System.out.println("[UserTestController] Adding question to test " + testId + " for user " + userId);
            
            // Check if user owns the test
            if (!testService.isTestOwner(testId, userId)) {
                return new ResponseEntity<>("You can only add questions to your own tests", HttpStatus.FORBIDDEN);
            }
            
            // Convert Object to QuestionDTO
            QuestionDTO questionDTO = objectMapper.convertValue(questionData, QuestionDTO.class);
            questionDTO.setTestId(testId);
            
            // Use the existing testService.addQuestion method
            QuestionDTO savedQuestion = testService.addQuestion(questionDTO);
            
            return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("[UserTestController] Error adding question to test " + testId + ": " + e.getMessage());
            e.printStackTrace();
            
            java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    
    // Update question in own test
    @PutMapping("/test/{testId}/question/{questionId}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> updateQuestionInMyTest(@PathVariable Long testId, @PathVariable Long questionId, @RequestBody Object questionData, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            System.out.println("[UserTestController] Updating question " + questionId + " in test " + testId + " for user " + userId);
            
            // Check if user owns the test
            if (!testService.isTestOwner(testId, userId)) {
                return new ResponseEntity<>("You can only update questions in your own tests", HttpStatus.FORBIDDEN);
            }
            
            // Convert Object to QuestionDTO
            QuestionDTO questionDTO = objectMapper.convertValue(questionData, QuestionDTO.class);
            questionDTO.setId(questionId);
            questionDTO.setTestId(testId);
            
            // Use the testService.updateQuestion method
            QuestionDTO updatedQuestion = testService.updateQuestion(questionDTO);
            
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("[UserTestController] Error updating question " + questionId + " in test " + testId + ": " + e.getMessage());
            e.printStackTrace();
            
            java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    
    // Delete question from own test
    @DeleteMapping("/test/{testId}/question/{questionId}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> deleteQuestionFromMyTest(@PathVariable Long testId, @PathVariable Long questionId, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            System.out.println("[UserTestController] Deleting question " + questionId + " from test " + testId + " for user " + userId);
            
            // Check if user owns the test
            if (!testService.isTestOwner(testId, userId)) {
                return new ResponseEntity<>("You can only delete questions from your own tests", HttpStatus.FORBIDDEN);
            }
            
            // Use the existing testService.deleteQuestion method
            testService.deleteQuestion(questionId);
            
            java.util.Map<String, String> response = new java.util.HashMap<>();
            response.put("message", "Question deleted successfully");
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("[UserTestController] Error deleting question " + questionId + " from test " + testId + ": " + e.getMessage());
            e.printStackTrace();
            
            java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    
    // Helper method to extract user ID from JWT token
    private Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractUsername(token);
            Long userId = jwtUtil.extractUserId(token);
            
            // If userId is not in token (legacy tokens), look up by email
            if (userId == null && email != null) {
                System.out.println("[UserTestController] Legacy token detected, looking up user by email: " + email);
                User user = userRepository.findFirstByEmail(email);
                if (user != null) {
                    return user.getId();
                } else {
                    throw new RuntimeException("User not found with email: " + email);
                }
            }
            
            if (userId == null) {
                throw new RuntimeException("Invalid token: missing user ID and email");
            }
            
            return userId;
        }
        throw new RuntimeException("Authorization token required");
    }
    
    @GetMapping("/question/types")
    public ResponseEntity<?> getQuestionTypes() {
        try {
            return new ResponseEntity<>(QuestionType.values(), HttpStatus.OK);
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
}
