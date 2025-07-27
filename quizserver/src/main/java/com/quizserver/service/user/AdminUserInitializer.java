package com.quizserver.service.user;

import com.quizserver.entities.User;
import com.quizserver.enums.UserRole;
import com.quizserver.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void createAdminUser() {
        User existing = userRepository.findByRole(UserRole.ADMIN);
        if (existing == null) {
            User user = new User();
            user.setName("Quiz Beta Admin");
            user.setEmail("admin@quizbeta.com");
            user.setRole(UserRole.ADMIN);
            user.setPassword(passwordEncoder.encode("quizbeta123"));
            userRepository.save(user);
            System.out.println("=== Quiz Beta Admin User Created ===");
            System.out.println("Email: admin@quizbeta.com");
            System.out.println("Password: quizbeta123");
            System.out.println("Database: quiz_beta_db");
            System.out.println("====================================");
        }
    }
}
