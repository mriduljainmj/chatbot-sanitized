package com.chatbot.project.dto;

public class PresidioRequest {
    private String text;

    public PresidioRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}