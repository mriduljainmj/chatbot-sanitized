package com.chatbot.project.controller;

import com.chatbot.project.dto.ChatSummary;
import com.chatbot.project.dto.MessageDto;
import com.chatbot.project.service.ChatService;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

	private final JdbcTemplate jdbcTemplate;
	private final ChatService chatService;

	public ChatController(JdbcTemplate jdbcTemplate, ChatService chatService) {
		this.jdbcTemplate = jdbcTemplate;
		this.chatService = chatService;
	}

	@PostMapping
	public String createChat(Authentication auth, String model) {
		return chatService.createChat(auth.getName(), model);
	}

	@GetMapping

	public List<ChatSummary> getChats(Authentication auth) {
		return jdbcTemplate.query("""
				    SELECT CHAT_ID, TITLE, MODEL
				    FROM CHATBOT_DB.APP.CHATS
				    WHERE USER_EMAIL = ?
				    ORDER BY CREATED_AT DESC
				""", (rs, i) -> new ChatSummary(rs.getString("CHAT_ID"), rs.getString("TITLE"), rs.getString("MODEL")),
				auth.getName());
	}

	@GetMapping("/{chatId}/messages")
	public List<MessageDto> getMessages(@PathVariable String chatId) {
		return jdbcTemplate.query("""
				    SELECT ROLE, CONTENT
				    FROM CHATBOT_DB.APP.MESSAGES
				    WHERE CHAT_ID = ?
				    ORDER BY CREATED_AT
				""", (rs, i) -> new MessageDto(rs.getString("ROLE"), rs.getString("CONTENT")), chatId);
	}

	@DeleteMapping("/{chatId}")
	public void deleteChat(@PathVariable String chatId) {

		jdbcTemplate.update("""
				    DELETE FROM CHATBOT_DB.APP.MESSAGES
				    WHERE CHAT_ID = ?
				""", chatId);

		jdbcTemplate.update("""
				    DELETE FROM CHATBOT_DB.APP.CHATS
				    WHERE CHAT_ID = ?
				""", chatId);
	}
}