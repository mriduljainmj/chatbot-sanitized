package com.chatbot.project.service;

import com.chatbot.project.dto.LoginRequest;
import com.chatbot.project.dto.SignupRequest;
import com.chatbot.project.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        String hash = passwordEncoder.encode(request.getPassword());
        userRepository.saveUser(request.getEmail(), hash);
    }

    public void login(LoginRequest request) {
        String storedHash =
                userRepository.findPasswordHashByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), storedHash)) {
            throw new RuntimeException("Invalid credentials");
        }
    }
}