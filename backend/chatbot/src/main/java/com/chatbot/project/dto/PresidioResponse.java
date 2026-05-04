package com.chatbot.project.dto;

import java.util.List;

public class PresidioResponse {

    private String sanitized_text;
    private List<Entity> entities;

    public String getSanitized_text() {
        return sanitized_text;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public static class Entity {
        private String entity_type;
        private int start;
        private int end;
        private double score;

        public String getEntity_type() {
            return entity_type;
        }

        public double getScore() {
            return score;
        }
    }
}