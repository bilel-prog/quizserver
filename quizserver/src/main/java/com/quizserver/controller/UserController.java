package com.quizserver.controller;

import com.quizserver.entities.User;
import com.quizserver.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Auth controller is working!");
        response.put("timestamp", new Date().toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody User user) {
        try {
            if (userService.hasUserWithEmail(user.getEmail())) {
                return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
            }
            User createUser = userService.createUser(user);
            if (createUser == null) {
                return new ResponseEntity<>("User not created, come again later", HttpStatus.NOT_ACCEPTABLE);
            }
            return new ResponseEntity<>(createUser, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal server error");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Map<String, Object> loginResponse = userService.login(user);
            if (loginResponse == null) {
                return new ResponseEntity<>("Wrong credentials", HttpStatus.NOT_ACCEPTABLE);
            }
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal server error");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
