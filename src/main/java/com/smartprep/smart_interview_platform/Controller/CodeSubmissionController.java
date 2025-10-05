package com.smartprep.smart_interview_platform.Controller;

import com.smartprep.smart_interview_platform.Model.CodeSubmission;
import com.smartprep.smart_interview_platform.Service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/submission")
public class CodeSubmissionController {

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    @PostMapping("/submit")
    public String submitCode(@RequestBody CodeSubmission codeSubmission){
        return codeSubmissionService.submitCode(codeSubmission);
    }

    @GetMapping("/user/{userId}")
    public List<CodeSubmission> getAllSubmissionsForUser(@PathVariable Long userId){
        return codeSubmissionService.getAllSubmissionsForUser(userId);
    }

    @GetMapping("/question/{questionId}")
    public List<CodeSubmission> getAllSubmissionsForQuestion(@PathVariable Long questionId){
        return codeSubmissionService.getAllSubmissionsForQuestion(questionId);
    }

    @GetMapping("/submission/{submissionId}")
    public Optional<CodeSubmission> getSubmissionById(@PathVariable Long submissionId){
        return codeSubmissionService.getSubmissionById(submissionId);
    }

    @PutMapping("/{submissionId}")
    public String updateSubmission(@PathVariable Long submissionId,@RequestBody CodeSubmission updatedSubmission){
        return codeSubmissionService.updateSubmission(submissionId,updatedSubmission);
    }

    @DeleteMapping("/{submissionId}")
    public String deleteSubmissionById(@PathVariable Long submissionId){
        return codeSubmissionService.deleteSubmissionById(submissionId);
    }

}
