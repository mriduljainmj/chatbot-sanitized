package com.chatbot.project.config;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SnowflakeConfig {

    private final DataSource dataSource;

    @Value("${snowflake.warehouse}")
    private String warehouse;

    @Value("${snowflake.database}")
    private String database;

    @Value("${snowflake.schema}")
    private String schema;

    public SnowflakeConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("USE WAREHOUSE " + warehouse);
            stmt.execute("USE DATABASE " + database);
            stmt.execute("USE SCHEMA " + schema);
        }
    }
}