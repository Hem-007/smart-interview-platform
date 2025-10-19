package com.smartprep.smart_interview_platform.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartprep.smart_interview_platform.Model.Question;
import com.smartprep.smart_interview_platform.Service.QuestionService;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/questions")
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String createQuestion(@RequestBody Question question){
        logger.info("[QUESTION] Creating new question: {}", question.getTitle());
        try {
            String result = questionService.createQuestion(question);
            logger.info("[QUESTION] Question created successfully: {}", question.getTitle());
            return result;
        } catch (Exception e) {
            logger.error("[QUESTION] Error creating question: {}", question.getTitle(), e);
            throw e;
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/all")
    public List<Question> getAllQuestions(){
        logger.info("[QUESTION] Fetching all questions");
        try {
            List<Question> questions = questionService.getAllQuestions();
            logger.debug("[QUESTION] Retrieved {} questions", questions.size());
            return questions;
        } catch (Exception e) {
            logger.error("[QUESTION] Error fetching all questions", e);
            throw e;
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/question/{id}")
    public Question getQuestionById(@PathVariable Long id){
        logger.info("[QUESTION] Fetching question by ID: {}", id);
        try {
            Question question = questionService.getQuestionById(id);
            logger.debug("[QUESTION] Question fetch completed for ID: {}", id);
            return question;
        } catch (Exception e) {
            logger.error("[QUESTION] Error fetching question by ID: {}", id, e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/question/{id}")
    public String updateQuestion(@PathVariable Long id,@RequestBody Question updatedQuestion){
        logger.info("[QUESTION] Updating question ID: {}", id);
        try {
            String result = questionService.updateQuestion(id, updatedQuestion);
            logger.info("[QUESTION] Question ID: {} updated successfully", id);
            return result;
        } catch (Exception e) {
            logger.error("[QUESTION] Error updating question ID: {}", id, e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/question/{id}")
    public String deleteQuestionById(@PathVariable Long id){
        logger.info("[QUESTION] Deleting question ID: {}", id);
        try {
            String result = questionService.deleteQuestionById(id);
            logger.info("[QUESTION] Question ID: {} deleted successfully", id);
            return result;
        } catch (Exception e) {
            logger.error("[QUESTION] Error deleting question ID: {}", id, e);
            throw e;
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<Question>> searchQuestions(@RequestParam String keyword) {
        logger.info("[QUESTION] Searching questions with keyword: {}", keyword);
        try {
            List<Question> results = questionService.searchQuestions(keyword);
            logger.debug("[QUESTION] Search returned {} results for keyword: {}", results.size(), keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("[QUESTION] Error searching questions with keyword: {}", keyword, e);
            throw e;
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/searchByTag")
    public ResponseEntity<List<Question>> searchByTag(@RequestParam String tag) {
        logger.info("[QUESTION] Searching questions by tag: {}", tag);
        try {
            List<Question> results = questionService.searchByTag(tag);
            logger.debug("[QUESTION] Search returned {} results for tag: {}", results.size(), tag);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("[QUESTION] Error searching questions by tag: {}", tag, e);
            throw e;
        }
    }
}
