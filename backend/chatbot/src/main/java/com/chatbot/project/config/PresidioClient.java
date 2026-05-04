package com.chatbot.project.config;

import com.chatbot.project.dto.PresidioRequest;
import com.chatbot.project.dto.PresidioResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PresidioClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public PresidioClient(
            RestTemplate restTemplate,
            @Value("${presidio.base-url:http://localhost:5000}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public PresidioResponse sanitize(String text) {
        PresidioRequest request = new PresidioRequest(text);
        return restTemplate.postForObject(
            baseUrl + "/sanitize",
            request,
            PresidioResponse.class
        );
    }
}