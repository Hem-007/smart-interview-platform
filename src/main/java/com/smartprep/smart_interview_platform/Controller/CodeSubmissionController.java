package com.smartprep.smart_interview_platform.Controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartprep.smart_interview_platform.Model.CodeSubmission;
import com.smartprep.smart_interview_platform.Service.CodeSubmissionService;

@RestController
@RequestMapping("/submission")
public class CodeSubmissionController {

    private static final Logger logger = LoggerFactory.getLogger(CodeSubmissionController.class);

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    @PostMapping("/submit")
    public String submitCode(@RequestBody CodeSubmission codeSubmission){
        logger.info("[SUBMISSION] Code submission received for user ID: {}, question ID: {}",
                   codeSubmission.getUser().getId(), codeSubmission.getQuestion().getId());
        try {
            String result = codeSubmissionService.submitCode(codeSubmission);
            logger.info("[SUBMISSION] Code submission processed successfully");
            return result;
        } catch (Exception e) {
            logger.error("[SUBMISSION] Error processing code submission", e);
            throw e;
        }
    }

    @GetMapping("/user/{userId}")
    public List<CodeSubmission> getAllSubmissionsForUser(@PathVariable Long userId){
        logger.info("[SUBMISSION] Fetching all submissions for user ID: {}", userId);
        try {
            List<CodeSubmission> submissions = codeSubmissionService.getAllSubmissionsForUser(userId);
            logger.debug("[SUBMISSION] Retrieved {} submissions for user ID: {}", submissions.size(), userId);
            return submissions;
        } catch (Exception e) {
            logger.error("[SUBMISSION] Error fetching submissions for user ID: {}", userId, e);
            throw e;
        }
    }

    @GetMapping("/question/{questionId}")
    public List<CodeSubmission> getAllSubmissionsForQuestion(@PathVariable Long questionId){
        logger.info("[SUBMISSION] Fetching all submissions for question ID: {}", questionId);
        try {
            List<CodeSubmission> submissions = codeSubmissionService.getAllSubmissionsForQuestion(questionId);
            logger.debug("[SUBMISSION] Retrieved {} submissions for question ID: {}", submissions.size(), questionId);
            return submissions;
        } catch (Exception e) {
            logger.error("[SUBMISSION] Error fetching submissions for question ID: {}", questionId, e);
            throw e;
        }
    }

    @GetMapping("/submission/{submissionId}")
    public Optional<CodeSubmission> getSubmissionById(@PathVariable Long submissionId){
        logger.info("[SUBMISSION] Fetching submission by ID: {}", submissionId);
        try {
            Optional<CodeSubmission> submission = codeSubmissionService.getSubmissionById(submissionId);
            logger.debug("[SUBMISSION] Submission fetch completed for ID: {}", submissionId);
            return submission;
        } catch (Exception e) {
            logger.error("[SUBMISSION] Error fetching submission by ID: {}", submissionId, e);
            throw e;
        }
    }

    @PutMapping("/{submissionId}")
    public String updateSubmission(@PathVariable Long submissionId,@RequestBody CodeSubmission updatedSubmission){
        logger.info("[SUBMISSION] Updating submission ID: {}", submissionId);
        try {
            String result = codeSubmissionService.updateSubmission(submissionId, updatedSubmission);
            logger.info("[SUBMISSION] Submission ID: {} updated successfully", submissionId);
            return result;
        } catch (Exception e) {
            logger.error("[SUBMISSION] Error updating submission ID: {}", submissionId, e);
            throw e;
        }
    }

    @DeleteMapping("/{submissionId}")
    public String deleteSubmissionById(@PathVariable Long submissionId){
        logger.info("[SUBMISSION] Deleting submission ID: {}", submissionId);
        try {
            String result = codeSubmissionService.deleteSubmissionById(submissionId);
            logger.info("[SUBMISSION] Submission ID: {} deleted successfully", submissionId);
            return result;
        } catch (Exception e) {
            logger.error("[SUBMISSION] Error deleting submission ID: {}", submissionId, e);
            throw e;
        }
    }

}
