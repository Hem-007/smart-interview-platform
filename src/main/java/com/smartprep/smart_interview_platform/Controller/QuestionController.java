package com.smartprep.smart_interview_platform.Controller;

import com.smartprep.smart_interview_platform.Model.Question;
import com.smartprep.smart_interview_platform.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String createQuestion(@RequestBody Question question){

        return questionService.createQuestion(question);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/all")
    public List<Question> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/question/{id}")
    public Question getQuestionById(@PathVariable Long id){
        return questionService.getQuestionById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/question/{id}")
    public String updateQuestion(@PathVariable Long id,@RequestBody Question updatedQuestion){
        return questionService.updateQuestion(id,updatedQuestion);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/question/{id}")
    public String deleteQuestionById(@PathVariable Long id){
        return questionService.deleteQuestionById(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<Question>> searchQuestions(@RequestParam String keyword) {
        return ResponseEntity.ok(questionService.searchQuestions(keyword));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/searchByTag")
    public ResponseEntity<List<Question>> searchByTag(@RequestParam String tag) {
        return ResponseEntity.ok(questionService.searchByTag(tag));
    }
}
