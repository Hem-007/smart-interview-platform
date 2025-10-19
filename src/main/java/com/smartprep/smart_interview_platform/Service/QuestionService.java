package com.smartprep.smart_interview_platform.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartprep.smart_interview_platform.Exception.ResourceNotFoundException;
import com.smartprep.smart_interview_platform.Model.Question;
import com.smartprep.smart_interview_platform.Repository.QuestionRepository;

@Service
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionRepository questionRepository;

    public String createQuestion(Question question){
        logger.info("[QUESTION_SERVICE] Creating new question: {}", question.getTitle());
        try {
            questionRepository.save(question);
            logger.info("[QUESTION_SERVICE] Question created successfully: {}", question.getTitle());
            return "Question Created Successfully !";
        } catch (Exception e) {
            logger.error("[QUESTION_SERVICE] Error creating question: {}", question.getTitle(), e);
            throw e;
        }
    }

    public List<Question> getAllQuestions(){
        logger.info("[QUESTION_SERVICE] Fetching all questions");
        try {
            List<Question> questions = questionRepository.findAll();
            logger.debug("[QUESTION_SERVICE] Retrieved {} questions", questions.size());
            return questions;
        } catch (Exception e) {
            logger.error("[QUESTION_SERVICE] Error fetching all questions", e);
            throw e;
        }
    }

    public Question getQuestionById(Long id){
        logger.info("[QUESTION_SERVICE] Fetching question by ID: {}", id);
        try {
            Optional<Question> optionalQuestion = questionRepository.findById(id);

            if(optionalQuestion.isPresent()){
                logger.debug("[QUESTION_SERVICE] Question found for ID: {}", id);
                return optionalQuestion.get();
            }
            else {
                logger.warn("[QUESTION_SERVICE] Question not found for ID: {}", id);
                throw new ResourceNotFoundException("Question is Not Found !");
            }
        } catch (Exception e) {
            logger.error("[QUESTION_SERVICE] Error fetching question by ID: {}", id, e);
            throw e;
        }
    }

    public String updateQuestion(Long id,Question updatedQuestion){
        logger.info("[QUESTION_SERVICE] Updating question ID: {}", id);
        try {
            Optional<Question> optionalQuestion = questionRepository.findById(id);

            if(optionalQuestion.isPresent()){
                Question existing = optionalQuestion.get();
                existing.setTitle(updatedQuestion.getTitle());
                existing.setDescription(updatedQuestion.getDescription());
                existing.setDifficulty(updatedQuestion.getDifficulty());
                existing.setTags(updatedQuestion.getTags());
                existing.setSampleInput(updatedQuestion.getSampleInput());
                existing.setSampleOutput(updatedQuestion.getSampleOutput());
                questionRepository.save(existing);
                logger.info("[QUESTION_SERVICE] Question ID: {} updated successfully", id);
                return "Question Updated Successfully !";
            }
            else {
                logger.warn("[QUESTION_SERVICE] Question not found for ID: {}", id);
                throw new ResourceNotFoundException("Question is Not Found !");
            }
        } catch (Exception e) {
            logger.error("[QUESTION_SERVICE] Error updating question ID: {}", id, e);
            throw e;
        }
    }

    public String deleteQuestionById(Long id){
        logger.info("[QUESTION_SERVICE] Deleting question ID: {}", id);
        try {
            Optional<Question> optionalQuestion = questionRepository.findById(id);

            if(optionalQuestion.isPresent()){
                questionRepository.deleteById(id);
                logger.info("[QUESTION_SERVICE] Question ID: {} deleted successfully", id);
                return "Question Deleted Successfully !";
            }
            else {
                logger.warn("[QUESTION_SERVICE] Question not found for ID: {}", id);
                throw new ResourceNotFoundException("Question is Not Found !");
            }
        } catch (Exception e) {
            logger.error("[QUESTION_SERVICE] Error deleting question ID: {}", id, e);
            throw e;
        }
    }

    public List<Question> searchQuestions(String keyword) {
        logger.info("[QUESTION_SERVICE] Searching questions with keyword: {}", keyword);
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                logger.warn("[QUESTION_SERVICE] Search failed - keyword is empty");
                throw new RuntimeException("Search keyword cannot be empty");
            }
            List<Question> results = questionRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
            logger.debug("[QUESTION_SERVICE] Search returned {} results for keyword: {}", results.size(), keyword);
            return results;
        } catch (Exception e) {
            logger.error("[QUESTION_SERVICE] Error searching questions with keyword: {}", keyword, e);
            throw e;
        }
    }

    public List<Question> searchByTag(String tag) {
        logger.info("[QUESTION_SERVICE] Searching questions by tag: {}", tag);
        try {
            if (tag == null || tag.trim().isEmpty()) {
                logger.warn("[QUESTION_SERVICE] Search failed - tag is empty");
                throw new RuntimeException("Search tag cannot be empty");
            }
            List<Question> results = questionRepository.findByTag(tag);
            logger.debug("[QUESTION_SERVICE] Search returned {} results for tag: {}", results.size(), tag);
            return results;
        } catch (Exception e) {
            logger.error("[QUESTION_SERVICE] Error searching questions by tag: {}", tag, e);
            throw e;
        }
    }

}

