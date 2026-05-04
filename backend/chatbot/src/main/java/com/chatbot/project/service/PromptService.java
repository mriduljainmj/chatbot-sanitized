package com.chatbot.project.service;

import org.springframework.stereotype.Service;

@Service
public class PromptService {

    private final SanitizationService sanitizationService;
    private final AiAgentService aiAgentService;

    public PromptService(
            SanitizationService sanitizationService,
            AiAgentService aiAgentService) {
        this.sanitizationService = sanitizationService;
        this.aiAgentService = aiAgentService;
    }

    public String processPrompt(String prompt,String model) {
    	
    	 String sanitizedPrompt =
                 sanitizationService.sanitize(prompt);
    	 return sanitizedPrompt;
      
//    	 switch (model) {
//    	        case "gpt-4o":
//    	            return callGpt4o(sanitizedPrompt);
//    	        case "gpt-3.5-turbo":
//    	            return callGpt35(sanitizedPrompt);
//    	        default:
//    	            throw new IllegalArgumentException("Unsupported model");
//    	    }


    }
}