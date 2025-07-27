package com.quizserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BasicHealthController {

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Server is running");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
