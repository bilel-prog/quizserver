package com.quizserver.service.user;

import com.quizserver.entities.User;
import com.quizserver.enums.UserRole;
import com.quizserver.repository.UserRepository;
import com.quizserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
            String token = jwtUtil.generateToken(optionalUser.getEmail(), optionalUser.getRole().toString());
            
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
}


