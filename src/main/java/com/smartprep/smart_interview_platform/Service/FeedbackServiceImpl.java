package com.smartprep.smart_interview_platform.Service;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * FeedbackServiceImpl - OpenAI GPT-4 Integration
 * 
 * Implements AI-based code feedback generation using OpenAI's GPT-4 API.
 * Provides intelligent analysis of code submissions with structured feedback on:
 * - Correctness and logic
 * - Time & space complexity
 * - Coding style and readability
 * - Improvement suggestions
 * 
 * Configuration:
 * - openai.api.key: Your OpenAI API key (from application.properties)
 * - Model: gpt-4 (can be changed to gpt-3.5-turbo for faster/cheaper responses)
 * 
 * @author Smart Interview Platform
 * @version 1.0
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    @Value("${openai.api.key:}")
    private String openaiApiKey;

    @Value("${openai.model:gpt-4}")
    private String model;

    @Value("${openai.max-tokens:1000}")
    private Integer maxTokens;

    @Value("${openai.temperature:0.7}")
    private Double temperature;

    /**
     * System prompt that guides GPT-4 to provide structured code feedback
     */
    private static final String SYSTEM_PROMPT = """
            You are an expert coding interviewer and software engineer. Your task is to review code submissions
            and provide constructive, structured feedback. Focus on:
            
            1. **Code Correctness**: Does the code solve the problem correctly? Are there any logical errors?
            2. **Time & Space Complexity**: Analyze the algorithm's efficiency. Suggest optimizations if needed.
            3. **Coding Style & Readability**: Is the code clean, well-structured, and easy to understand?
            4. **Best Practices**: Does the code follow language conventions and best practices?
            5. **Improvement Suggestions**: Provide specific, actionable suggestions for improvement.
            
            Format your response as clear, concise text without JSON or markdown formatting.
            Be encouraging but honest. Keep feedback to 500 words maximum.
            """;

    /**
     * Generates AI-based feedback for submitted code using OpenAI GPT-4.
     * 
     * @param code The source code to analyze
     * @param language The programming language (e.g., "Java", "Python", "C++")
     * @return Clean text feedback from GPT-4, or null if generation fails
     * @throws IllegalArgumentException if code or language is null/empty
     * @throws RuntimeException if API key is not configured or API call fails
     */
    @Override
    public String generateFeedback(String code, String language) {
        logger.info("[FEEDBACK_SERVICE] Generating AI feedback for {} code submission", language);

        // Validate inputs
        if (code == null || code.trim().isEmpty()) {
            logger.warn("[FEEDBACK_SERVICE] Code submission is empty");
            throw new IllegalArgumentException("Code cannot be null or empty");
        }

        if (language == null || language.trim().isEmpty()) {
            logger.warn("[FEEDBACK_SERVICE] Programming language not specified");
            throw new IllegalArgumentException("Programming language cannot be null or empty");
        }

        // Check if API key is configured
        if (openaiApiKey == null || openaiApiKey.trim().isEmpty()) {
            logger.error("[FEEDBACK_SERVICE] OpenAI API key is not configured");
            throw new RuntimeException("OpenAI API key is not configured. Set 'openai.api.key' in application.properties");
        }

        try {
            logger.debug("[FEEDBACK_SERVICE] Initializing OpenAI service with model: {}", model);
            
            // Initialize OpenAI service with API key
            OpenAiService service = new OpenAiService(openaiApiKey);

            // Build the user message with code and language context
            String userMessage = String.format("""
                    Please review the following %s code and provide structured feedback:
                    
                    ```%s
                    %s
                    ```
                    """, language, language.toLowerCase(), code);

            logger.debug("[FEEDBACK_SERVICE] Sending request to OpenAI API");

            // Create chat completion request
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(buildMessages(userMessage))
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .build();

            // Call OpenAI API
            var response = service.createChatCompletion(request);

            // Extract feedback from response
            String feedback = response.getChoices()
                    .stream()
                    .findFirst()
                    .map(choice -> choice.getMessage().getContent())
                    .orElse(null);

            if (feedback == null || feedback.trim().isEmpty()) {
                logger.warn("[FEEDBACK_SERVICE] Empty response received from OpenAI API");
                return "Unable to generate feedback at this time. Please try again later.";
            }

            logger.info("[FEEDBACK_SERVICE] AI feedback generated successfully for {} code", language);
            logger.debug("[FEEDBACK_SERVICE] Feedback length: {} characters", feedback.length());

            return feedback.trim();

        } catch (IllegalArgumentException e) {
            logger.error("[FEEDBACK_SERVICE] Invalid input for feedback generation", e);
            throw e;
        } catch (Exception e) {
            logger.error("[FEEDBACK_SERVICE] Error generating feedback from OpenAI API", e);
            // Return graceful error message instead of throwing
            return "AI feedback generation encountered an error. Please try again later.";
        }
    }

    /**
     * Builds the message list for ChatGPT API call.
     * Includes system prompt and user message.
     * 
     * @param userMessage The user's code review request
     * @return List of ChatMessage objects for API request
     */
    private List<ChatMessage> buildMessages(String userMessage) {
        List<ChatMessage> messages = new ArrayList<>();
        
        // Add system prompt to guide the AI behavior
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), SYSTEM_PROMPT));
        
        // Add user's code review request
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), userMessage));
        
        return messages;
    }

}

