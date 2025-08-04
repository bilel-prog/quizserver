package com.quizserver.service.user;

import com.quizserver.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Map;

public interface UserService {
    User createUser(User user);

    boolean hasUserWithEmail(String email);
    
    Map<String, Object> login(User user);
    
    // User management methods for admin
    Page<User> getAllUsersPaged(Pageable pageable);
    
    void deleteUser(Long userId);
    
    User getUserById(Long userId);
}
