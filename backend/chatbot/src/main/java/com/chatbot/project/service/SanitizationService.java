
package com.chatbot.project.service;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

import org.springframework.stereotype.Service;

import com.chatbot.project.config.PresidioClient;
import com.chatbot.project.dto.PresidioResponse;

import jakarta.annotation.PostConstruct;

import java.io.InputStream;
import java.util.regex.Pattern;

@Service
public class SanitizationService {

	private final PresidioClient presidioClient;

	public SanitizationService(PresidioClient presidioClient) {
		this.presidioClient = presidioClient;
	}

	// ✅ HARD PII (deterministic)
	private static final Pattern EMAIL = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+");

	private static final Pattern PHONE = Pattern.compile("\\b(?:\\+91[-\\s]?)?[6-9]\\d{9}\\b");

	private static final Pattern CREDIT_CARD = Pattern.compile("\\b\\d{4}[- ]?\\d{4}[- ]?\\d{4}[- ]?\\d{4}\\b");

	// ✅ NAME HEURISTICS (better than NLP for chat)
	private static final Pattern NAME_PATTERN = Pattern.compile("\\b([A-Z][a-z]{2,}\\s[A-Z][a-z]{2,})\\b");

	public String sanitize(String input) {

		String text = input;

		// 1️⃣ Hard PII
		text = EMAIL.matcher(text).replaceAll("[EMAIL]");
		text = PHONE.matcher(text).replaceAll("[PHONE]");
		text = CREDIT_CARD.matcher(text).replaceAll("[CARD]");

		// 2️⃣ Name heuristic
		text = NAME_PATTERN.matcher(text).replaceAll("[PERSON]");

		// 3️⃣ Optional: context-based replacement
		text = text.replaceAll("(?i)my name is\\s+\\[PERSON\\]", "my name is [PERSON]");

		try {
			// 2️⃣ Authoritative Presidio sanitization
			PresidioResponse response = presidioClient.sanitize(text);

			if (response != null && response.getSanitized_text() != null) {
				return response.getSanitized_text();
			}

		} catch (Exception e) {
			// ✅ Fail safely (never leak sensitive data)
			System.err.println("Presidio unavailable, using local sanitization");
		}

		return text;
	}

}
