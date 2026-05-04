package com.chatbot.project.controller;

import com.chatbot.project.dto.PromptRequest;
import com.chatbot.project.dto.PromptResponse;
import com.chatbot.project.service.ChatService;
import com.chatbot.project.service.PromptService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PromptController {

	private final PromptService promptService;
	private final ChatService chatService;

	public PromptController(PromptService promptService, ChatService chatService) {
		this.promptService = promptService;
		this.chatService = chatService;
	}

	@PostMapping("/prompt")
	public PromptResponse prompt(
	        @RequestBody PromptRequest request,
	        Authentication auth) {

	    String userInput = request.getPrompt();
	    String chatId = request.getChatId();
	    String requestedModel = request.getModel(); // from frontend

	    // ✅ CREATE CHAT IF NEEDED
	    if (chatId == null) {
	        chatId = chatService.createChat(auth.getName(), requestedModel);
	    } else {
	        // ✅ ENFORCE SAME MODEL
	        String storedModel = chatService.getChatModel(chatId);
	        if (!storedModel.equals(requestedModel)) {
	            throw new ResponseStatusException(
	                HttpStatus.BAD_REQUEST,
	                "AI model change is not allowed for this chat"
	            );
	        }
	    }

	    chatService.saveMessage(chatId, "USER", userInput);
	    chatService.updateChatTitleIfEmpty(chatId, userInput);

	    String aiResponse =
	        promptService.processPrompt(userInput, requestedModel);

	    chatService.saveMessage(chatId, "AI", aiResponse);

	    return new PromptResponse(aiResponse, chatId);
	}


}