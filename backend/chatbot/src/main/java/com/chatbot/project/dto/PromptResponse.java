package com.chatbot.project.dto;

public class PromptResponse {

    private String response;
    private String chatId;

    // ✅ NEW constructor (this fixes the error)
    public PromptResponse(String response, String chatId) {
        this.response = response;
        this.chatId = chatId;
    }

    public String getResponse() {
        return response;
    }

    public String getChatId() {
        return chatId;
    }
}