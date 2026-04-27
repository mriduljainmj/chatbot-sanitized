package com.chatbot.project.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveUser(String email, String passwordHash) {
        jdbcTemplate.update("""
            INSERT INTO USERS (EMAIL, PASSWORD_HASH)
            VALUES (?, ?)
        """, email, passwordHash);
    }

    public String findPasswordHashByEmail(String email) {
        return jdbcTemplate.queryForObject("""
            SELECT PASSWORD_HASH FROM USERS WHERE EMAIL = ?
        """, String.class, email);
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject("""
            SELECT COUNT(*) FROM USERS WHERE EMAIL = ?
        """, Integer.class, email);
        return count != null && count > 0;
    }
}