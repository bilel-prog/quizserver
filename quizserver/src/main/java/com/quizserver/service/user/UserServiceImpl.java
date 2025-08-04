package com.quizserver.service.user;

import com.quizserver.entities.User;
import com.quizserver.entities.Test;
import com.quizserver.entities.Question;
import com.quizserver.enums.UserRole;
import com.quizserver.repository.UserRepository;
import com.quizserver.repository.TestResultRepository;
import com.quizserver.repository.TestRepository;
import com.quizserver.repository.QuestionRepository;
import com.quizserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TestResultRepository testResultRepository;
    
    @Autowired
    private TestRepository testRepository;
    
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }

    @Override
    public User createUser(User user) {
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Map<String, Object> login(User user) {
        User optionalUser = userRepository.findFirstByEmail(user.getEmail());
        if (optionalUser != null && passwordEncoder.matches(user.getPassword(), optionalUser.getPassword())) {
            String token = jwtUtil.generateToken(optionalUser.getEmail(), optionalUser.getRole().toString(), optionalUser.getId());
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", optionalUser.getId());
            response.put("name", optionalUser.getName());
            response.put("email", optionalUser.getEmail());
            response.put("role", optionalUser.getRole().toString());
            response.put("token", token);
            
            return response;
        }
        return null;
    }
    
    @Override
    public Page<User> getAllUsersPaged(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Prevent deletion of admin users and ensure at least one admin remains
            if (user.getRole() == UserRole.ADMIN) {
                // Count total admin users
                long adminCount = userRepository.countByRole(UserRole.ADMIN);
                if (adminCount <= 1) {
                    throw new RuntimeException("Cannot delete the last admin user. At least one admin must remain in the system.");
                }
                throw new RuntimeException("Cannot delete admin users for security reasons.");
            }
            
            // Step 1: Delete all test results for this user (both as test taker and for tests they created)
            testResultRepository.deleteAll(testResultRepository.findAllByUserId(userId));
            
            // Step 2: Get all tests created by this user
            List<Test> userTests = testRepository.findByCreatedById(userId);
            
            // Step 3: For each test created by the user, delete all its questions first
            for (Test test : userTests) {
                // Delete all test results for this test (in case other users took it)
                testResultRepository.deleteAll(testResultRepository.findAllByTestId(test.getId()));
                
                // Delete all questions for this test
                questionRepository.deleteAll(questionRepository.findByTest(test, org.springframework.data.domain.Pageable.unpaged()).getContent());
            }
            
            // Step 4: Delete all tests created by this user
            testRepository.deleteAll(userTests);
            
            // Step 5: Finally delete the user
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }
    
    @Override
    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }
}


