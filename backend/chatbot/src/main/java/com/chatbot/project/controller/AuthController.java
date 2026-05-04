package com.chatbot.project.controller;

import com.chatbot.project.dto.LoginRequest;
import com.chatbot.project.dto.LoginResponse;
import com.chatbot.project.dto.SignupRequest;
import com.chatbot.project.service.AuthService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
		authService.signup(request);
		return ResponseEntity.ok("Signup successful");
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
	        @Valid @RequestBody LoginRequest request) {

	    LoginResponse response = authService.login(request);
	    return ResponseEntity.ok(response);
	}
	
	


}