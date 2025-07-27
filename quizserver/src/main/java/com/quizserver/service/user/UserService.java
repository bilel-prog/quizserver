package com.quizserver.service.user;

import com.quizserver.entities.User;
import java.util.Map;

public interface UserService {
    User createUser(User user);

    boolean hasUserWithEmail(String email);
    
    Map<String, Object> login(User user); 
}
