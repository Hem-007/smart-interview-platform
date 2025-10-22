package com.smartprep.smart_interview_platform.Service;

/**
 * FeedbackService Interface
 * 
 * Defines contract for AI-based code feedback generation.
 * Implementations should integrate with AI APIs (e.g., OpenAI GPT-4) to provide
 * intelligent feedback on code submissions.
 * 
 * @author Smart Interview Platform
 * @version 1.0
 */
public interface FeedbackService {

    /**
     * Generates AI-based feedback for submitted code.
     * 
     * Analyzes the provided code and returns structured feedback on:
     * - Code correctness and logic
     * - Time and space complexity efficiency
     * - Coding style and readability
     * - Suggestions for improvement
     * 
     * @param code The source code to analyze (required)
     * @param language The programming language of the code (e.g., "Java", "Python", "C++")
     * @return Clean text feedback without JSON clutter, or null if feedback generation fails
     * @throws IllegalArgumentException if code or language is null/empty
     * @throws RuntimeException if API communication fails
     * 
     * Example:
     * String feedback = feedbackService.generateFeedback(
     *     "public int add(int a, int b) { return a + b; }",
     *     "Java"
     * );
     */
    String generateFeedback(String code, String language);

}

