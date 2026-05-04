package com.chatbot.project.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatService {

	private final JdbcTemplate jdbcTemplate;

	public ChatService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String createChat(String userEmail, String model) {
	    String chatId = UUID.randomUUID().toString();

	    jdbcTemplate.update("""
	        INSERT INTO CHATBOT_DB.APP.CHATS (CHAT_ID, USER_EMAIL, TITLE, MODEL)
	        VALUES (?, ?, ?, ?)
	    """, chatId, userEmail, "New Chat", model);

	    return chatId;
	}
	
	public String getChatModel(String chatId) {
	    return jdbcTemplate.queryForObject("""
	        SELECT MODEL
	        FROM CHATBOT_DB.APP.CHATS
	        WHERE CHAT_ID = ?
	    """, String.class, chatId);
	}

	public void saveMessage(String chatId, String role, String content) {
		jdbcTemplate.update("""
				    INSERT INTO CHATBOT_DB.APP.MESSAGES (CHAT_ID, ROLE, CONTENT)
				    VALUES (?, ?, ?)
				""", chatId, role, content);
	}

	public void updateChatTitleIfEmpty(String chatId, String userMessage) {

		String title = userMessage.replaceAll("\\s+", " ").trim();

		// Keep title short (UX-friendly)
		if (title.length() > 40) {
			title = title.substring(0, 40) + "...";
		}

		jdbcTemplate.update("""
				    UPDATE CHATBOT_DB.APP.CHATS
				    SET TITLE = ?
				    WHERE CHAT_ID = ?
				      AND TITLE = 'New Chat'
				""", title, chatId);
	}
}