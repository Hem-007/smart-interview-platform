package com.smartprep.smart_interview_platform.Service;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for FeedbackServiceImpl
 * 
 * Tests the AI feedback generation functionality with mocked OpenAI API responses.
 * Covers success cases, error handling, and edge cases.
 * 
 * @author Smart Interview Platform
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    @Mock
    private OpenAiService openAiService;

    private static final String TEST_CODE = "public int add(int a, int b) { return a + b; }";
    private static final String TEST_LANGUAGE = "Java";
    private static final String TEST_FEEDBACK = "Your code is correct and efficient. Good job!";

    @BeforeEach
    void setUp() {
        // Set API key via reflection to avoid needing application.properties in tests
        ReflectionTestUtils.setField(feedbackService, "openaiApiKey", "test-api-key");
        ReflectionTestUtils.setField(feedbackService, "model", "gpt-4");
        ReflectionTestUtils.setField(feedbackService, "maxTokens", 1000);
        ReflectionTestUtils.setField(feedbackService, "temperature", 0.7);
    }

    /**
     * Test successful feedback generation
     */
    @Test
    void testGenerateFeedback_Success() {
        // Arrange
        ChatCompletionResult mockResult = createMockChatCompletionResult(TEST_FEEDBACK);

        // Act
        String feedback = feedbackService.generateFeedback(TEST_CODE, TEST_LANGUAGE);

        // Assert
        assertNotNull(feedback);
        assertEquals(TEST_FEEDBACK, feedback);
    }

    /**
     * Test feedback generation with null code
     */
    @Test
    void testGenerateFeedback_NullCode() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> feedbackService.generateFeedback(null, TEST_LANGUAGE)
        );
        assertEquals("Code cannot be null or empty", exception.getMessage());
    }

    /**
     * Test feedback generation with empty code
     */
    @Test
    void testGenerateFeedback_EmptyCode() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> feedbackService.generateFeedback("   ", TEST_LANGUAGE)
        );
        assertEquals("Code cannot be null or empty", exception.getMessage());
    }

    /**
     * Test feedback generation with null language
     */
    @Test
    void testGenerateFeedback_NullLanguage() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> feedbackService.generateFeedback(TEST_CODE, null)
        );
        assertEquals("Programming language cannot be null or empty", exception.getMessage());
    }

    /**
     * Test feedback generation with empty language
     */
    @Test
    void testGenerateFeedback_EmptyLanguage() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> feedbackService.generateFeedback(TEST_CODE, "   ")
        );
        assertEquals("Programming language cannot be null or empty", exception.getMessage());
    }

    /**
     * Test feedback generation with missing API key
     */
    @Test
    void testGenerateFeedback_MissingApiKey() {
        // Arrange
        ReflectionTestUtils.setField(feedbackService, "openaiApiKey", "");

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> feedbackService.generateFeedback(TEST_CODE, TEST_LANGUAGE)
        );
        assertTrue(exception.getMessage().contains("OpenAI API key is not configured"));
    }

    /**
     * Test feedback generation with different programming languages
     */
    @Test
    void testGenerateFeedback_DifferentLanguages() {
        String[] languages = {"Python", "C++", "JavaScript", "Go", "Rust"};

        for (String language : languages) {
            // Arrange
            ChatCompletionResult mockResult = createMockChatCompletionResult(TEST_FEEDBACK);

            // Act
            String feedback = feedbackService.generateFeedback(TEST_CODE, language);

            // Assert
            assertNotNull(feedback);
            assertEquals(TEST_FEEDBACK, feedback);
        }
    }

    /**
     * Test feedback generation with long code
     */
    @Test
    void testGenerateFeedback_LongCode() {
        // Arrange
        StringBuilder longCode = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longCode.append("int var").append(i).append(" = ").append(i).append(";\n");
        }
        ChatCompletionResult mockResult = createMockChatCompletionResult(TEST_FEEDBACK);

        // Act
        String feedback = feedbackService.generateFeedback(longCode.toString(), TEST_LANGUAGE);

        // Assert
        assertNotNull(feedback);
        assertEquals(TEST_FEEDBACK, feedback);
    }

    /**
     * Test feedback generation with special characters in code
     */
    @Test
    void testGenerateFeedback_SpecialCharacters() {
        // Arrange
        String codeWithSpecialChars = "String str = \"Hello\\nWorld\\t!\"; // Comment with special chars: @#$%";
        ChatCompletionResult mockResult = createMockChatCompletionResult(TEST_FEEDBACK);

        // Act
        String feedback = feedbackService.generateFeedback(codeWithSpecialChars, TEST_LANGUAGE);

        // Assert
        assertNotNull(feedback);
        assertEquals(TEST_FEEDBACK, feedback);
    }

    /**
     * Test feedback generation with empty API response
     */
    @Test
    void testGenerateFeedback_EmptyApiResponse() {
        // Arrange
        ChatCompletionResult mockResult = createMockChatCompletionResult("");

        // Act
        String feedback = feedbackService.generateFeedback(TEST_CODE, TEST_LANGUAGE);

        // Assert
        assertNotNull(feedback);
        assertEquals("Unable to generate feedback at this time. Please try again later.", feedback);
    }

    /**
     * Test feedback generation with API error
     */
    @Test
    void testGenerateFeedback_ApiError() {
        // Arrange
        ReflectionTestUtils.setField(feedbackService, "openaiApiKey", "test-api-key");

        // Act
        String feedback = feedbackService.generateFeedback(TEST_CODE, TEST_LANGUAGE);

        // Assert
        assertNotNull(feedback);
        assertEquals("AI feedback generation encountered an error. Please try again later.", feedback);
    }

    /**
     * Test feedback generation with whitespace-only feedback
     */
    @Test
    void testGenerateFeedback_WhitespaceOnlyFeedback() {
        // Arrange
        ChatCompletionResult mockResult = createMockChatCompletionResult("   \n\t  ");

        // Act
        String feedback = feedbackService.generateFeedback(TEST_CODE, TEST_LANGUAGE);

        // Assert
        assertNotNull(feedback);
        assertEquals("Unable to generate feedback at this time. Please try again later.", feedback);
    }

    /**
     * Helper method to create mock ChatCompletionResult
     */
    private ChatCompletionResult createMockChatCompletionResult(String content) {
        ChatCompletionResult result = new ChatCompletionResult();
        List<ChatCompletionChoice> choices = new ArrayList<>();
        
        ChatCompletionChoice choice = new ChatCompletionChoice();
        ChatMessage message = new ChatMessage();
        message.setContent(content);
        choice.setMessage(message);
        choices.add(choice);
        
        result.setChoices(choices);
        return result;
    }

}

