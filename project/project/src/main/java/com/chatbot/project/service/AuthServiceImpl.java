package com.chatbot.project.service;

import com.chatbot.project.dto.LoginRequest;
import com.chatbot.project.dto.LoginResponse;
import com.chatbot.project.entity.User;
import com.chatbot.project.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new LoginResponse(
                "Login successful",
                user.getEmail(),
                user.getRole()
        );
    }

    @Override
    public String logout() {
        return "Logout successful";
    }
}

