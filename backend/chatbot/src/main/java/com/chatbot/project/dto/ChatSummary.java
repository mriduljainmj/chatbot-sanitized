package com.chatbot.project.dto;

public class ChatSummary {
    private String chatId;
    private String title;
    private String model;

    public ChatSummary(String chatId, String title, String model) {
        this.chatId = chatId;
        this.title = title;
        this.model = model;
    }

    public String getChatId() { return chatId; }
    public String getTitle() { return title; }
    public String getModel() { return model; }
}