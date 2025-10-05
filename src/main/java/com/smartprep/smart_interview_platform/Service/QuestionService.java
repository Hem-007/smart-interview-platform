package com.smartprep.smart_interview_platform.Service;

import com.smartprep.smart_interview_platform.Exception.ResourceNotFoundException;
import com.smartprep.smart_interview_platform.Model.Question;
import com.smartprep.smart_interview_platform.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public String createQuestion(Question question){
        questionRepository.save(question);
        return "Question Created Successfully !";
    }

    public List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id){
        Optional<Question> optionalQuestion = questionRepository.findById(id);

        if(optionalQuestion.isPresent()){
            return optionalQuestion.get();
        }
        else {
            throw new ResourceNotFoundException("Question is Not Found !");
        }
    }

    public String updateQuestion(Long id,Question updatedQuestion){
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
            return "Question Updated Successfully !";
        }
        else {
            throw new ResourceNotFoundException("Question is Not Found !");
        }
    }

    public String deleteQuestionById(Long id){
        Optional<Question> optionalQuestion = questionRepository.findById(id);

        if(optionalQuestion.isPresent()){
            questionRepository.deleteById(id);
            return "Question Deleted Successfully !";
        }
        else {
            throw new ResourceNotFoundException("Question is Not Found !");
        }
    }

    public List<Question> searchQuestions(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new RuntimeException("Search keyword cannot be empty");
        }
        return questionRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    public List<Question> searchByTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new RuntimeException("Search tag cannot be empty");
        }
        return questionRepository.findByTag(tag);
    }

}

