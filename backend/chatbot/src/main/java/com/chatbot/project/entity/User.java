package com.chatbot.project.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
public class User {

    @Id
    private String id;

    private String email;
    private String password;
    private String role;
}


