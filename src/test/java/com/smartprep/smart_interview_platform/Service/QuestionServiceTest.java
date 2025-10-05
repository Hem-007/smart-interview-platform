package com.smartprep.smart_interview_platform.Service;
import com.smartprep.smart_interview_platform.Exception.ResourceNotFoundException;
import com.smartprep.smart_interview_platform.Model.Question;
import com.smartprep.smart_interview_platform.Repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question sampleQuestion;

    @BeforeEach
    void setUp() {
        sampleQuestion = new Question(
                1L,
                "Two Sum",
                "Find two numbers that add up to target.",
                "Easy",
                Arrays.asList("array", "math"),
                "2,7,11,15; target=9",
                "[0,1]"
        );
    }

    // 1. createQuestion
    @Test
    void testCreateQuestion_Success() {
        when(questionRepository.save(any(Question.class))).thenReturn(sampleQuestion);

        String result = questionService.createQuestion(sampleQuestion);

        assertEquals("Question Created Successfully !", result);
        verify(questionRepository, times(1)).save(sampleQuestion);
    }

    // 2. getQuestionById
    @Test
    void testGetQuestionById_Success() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(sampleQuestion));

        Question result = questionService.getQuestionById(1L);

        assertNotNull(result);
        assertEquals("Two Sum", result.getTitle());
        verify(questionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetQuestionById_NotFound() {
        when(questionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> questionService.getQuestionById(99L));
    }

    // 3. getAllQuestions
    @Test
    void testGetAllQuestions_WithData() {
        List<Question> questions = Arrays.asList(sampleQuestion, new Question());
        when(questionRepository.findAll()).thenReturn(questions);

        List<Question> result = questionService.getAllQuestions();

        assertEquals(2, result.size());
        verify(questionRepository, times(1)).findAll();
    }

    @Test
    void testGetAllQuestions_Empty() {
        when(questionRepository.findAll()).thenReturn(Collections.emptyList());

        List<Question> result = questionService.getAllQuestions();

        assertTrue(result.isEmpty());
    }

    // 4. deleteQuestionById
    @Test
    void testDeleteQuestionById_Success() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(sampleQuestion));

        String result = questionService.deleteQuestionById(1L);

        assertEquals("Question Deleted Successfully !", result);
        verify(questionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteQuestionById_NotFound() {
        when(questionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> questionService.deleteQuestionById(99L));
    }

    // 5. updateQuestion (if implemented)
    @Test
    void testUpdateQuestion_Success() {
        Question updated = new Question(
                1L,
                "Updated Title",
                "Updated Desc",
                "Medium",
                Arrays.asList("updated"),
                "input",
                "output"
        );

        when(questionRepository.findById(1L)).thenReturn(Optional.of(sampleQuestion));
        when(questionRepository.save(any(Question.class))).thenReturn(updated);

        String result = questionService.updateQuestion(1L, updated);

        assertEquals("Question Updated Successfully !", result);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void testUpdateQuestion_NotFound() {
        Question updated = new Question();
        updated.setTitle("Updated Title");

        when(questionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> questionService.updateQuestion(99L, updated));
    }

    // 6. searchQuestions (if implemented)
    @Test
    void testSearchQuestions_WithResults() {
        when(questionRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("two", "two"))
                .thenReturn(Collections.singletonList(sampleQuestion));

        List<Question> result = questionService.searchQuestions("two");

        assertEquals(1, result.size());
        assertEquals("Two Sum", result.get(0).getTitle());
    }

    @Test
    void testSearchQuestions_NoResults() {
        when(questionRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("xyz", "xyz"))
                .thenReturn(Collections.emptyList());

        List<Question> result = questionService.searchQuestions("xyz");

        assertTrue(result.isEmpty());
    }
}
