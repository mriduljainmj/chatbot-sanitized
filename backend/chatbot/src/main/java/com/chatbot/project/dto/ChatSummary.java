package com.chatbot.project.dto;

public class ChatSummary {
    private String chatId;
    private String title;

    public ChatSummary(String chatId, String title) {
        this.chatId = chatId;
        this.title = title;
    }

    public String getChatId() { return chatId; }
    public String getTitle() { return title; }
}
