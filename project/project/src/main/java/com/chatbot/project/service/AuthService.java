package com.chatbot.project.service;

import com.chatbot.project.dto.LoginRequest;
import com.chatbot.project.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    String logout();
}
