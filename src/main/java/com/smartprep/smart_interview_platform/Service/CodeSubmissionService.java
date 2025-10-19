package com.smartprep.smart_interview_platform.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartprep.smart_interview_platform.Model.CodeSubmission;
import com.smartprep.smart_interview_platform.Model.Question;
import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Model.UserStats;
import com.smartprep.smart_interview_platform.Repository.CodeSubmissionRepository;
import com.smartprep.smart_interview_platform.Repository.QuestionRepository;
import com.smartprep.smart_interview_platform.Repository.UserRepository;
import com.smartprep.smart_interview_platform.Repository.UserStatsRepository;

@Service
public class CodeSubmissionService {

    private static final Logger logger = LoggerFactory.getLogger(CodeSubmissionService.class);
    @Autowired
    private CodeSubmissionRepository codeSubmissionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatsRepository userStatsRepository;

    public String submitCode(CodeSubmission codeSubmission) {
        logger.info("[CODE_SUBMISSION] Code submission received");
        try {
            if (codeSubmission.getUser() == null || codeSubmission.getUser().getId() == null) {
                logger.warn("[CODE_SUBMISSION] Submission rejected - User is missing or User ID is null");
                return "User is missing or User ID is null!";
            }

            if (codeSubmission.getQuestion() == null || codeSubmission.getQuestion().getId() == null) {
                logger.warn("[CODE_SUBMISSION] Submission rejected - Question is missing or Question ID is null");
                return "Question is missing or Question ID is null!";
            }

            Optional<User> optionalUser = userRepository.findById(codeSubmission.getUser().getId());
            if (optionalUser.isEmpty()) {
                logger.warn("[CODE_SUBMISSION] Submission rejected - Invalid User ID: {}", codeSubmission.getUser().getId());
                return "Invalid User ID or User not found!";
            }

            Optional<Question> optionalQuestion = questionRepository.findById(codeSubmission.getQuestion().getId());
            if (optionalQuestion.isEmpty()) {
                logger.warn("[CODE_SUBMISSION] Submission rejected - Invalid Question ID: {}", codeSubmission.getQuestion().getId());
                return "Invalid Question ID or Question not found!";
            }

            // Set the actual User and Question from DB to avoid transient object errors
            codeSubmission.setUser(optionalUser.get());
            codeSubmission.setQuestion(optionalQuestion.get());
            codeSubmission.setSubmittedAt(LocalDateTime.now());
            logger.debug("[CODE_SUBMISSION] Validating submission for user: {}, question: {}",
                        optionalUser.get().getId(), optionalQuestion.get().getId());

            boolean firstSolve = codeSubmissionRepository.existsByUserAndQuestionAndStatus(codeSubmission.getUser(),codeSubmission.getQuestion(),"Accepted");

            codeSubmissionRepository.save(codeSubmission);
            logger.debug("[CODE_SUBMISSION] Submission saved to database");

            //Fetching the Stats
            Optional<UserStats> optionalUserStats = userStatsRepository.findById(codeSubmission.getUser().getId());
            UserStats stats;
            if(optionalUserStats.isPresent()) {
                stats = optionalUserStats.get();
            }else {
                stats = new UserStats();
                stats.setUser(codeSubmission.getUser());
            }

            //Setting the total Submission
            stats.setTotalSubmissions(stats.getTotalSubmissions() + 1);

            //Setting the Accepted Submission
            if(codeSubmission.getStatus().equalsIgnoreCase("Accepted")){
                stats.setAcceptedSubmissions(stats.getAcceptedSubmissions()+1);
                logger.debug("[CODE_SUBMISSION] Submission marked as Accepted");
            }

            //Setting the Questions Solved
            logger.debug("[CODE_SUBMISSION] First solve status: {}", firstSolve);
            if(!firstSolve){
                logger.info("[CODE_SUBMISSION] First solve detected for user: {}, question: {}",
                           optionalUser.get().getId(), optionalQuestion.get().getId());
                stats.setQuestionsSolved(stats.getQuestionsSolved()+1);
            }

            Double accuracy = (stats.getAcceptedSubmissions() * 100.0 ) / stats.getTotalSubmissions();
            stats.setAccuracy(accuracy);
            logger.debug("[CODE_SUBMISSION] User accuracy updated to: {}%", accuracy);

            userStatsRepository.save(stats);
            logger.info("[CODE_SUBMISSION] User stats updated successfully");

            String result = "✅ Code successfully submitted by " + optionalUser.get().getName() +
                    " for the question \"" + optionalQuestion.get().getTitle() + "\"";
            logger.info("[CODE_SUBMISSION] Submission completed successfully");
            return result;
        } catch (Exception e) {
            logger.error("[CODE_SUBMISSION] Error processing code submission", e);
            throw e;
        }
    }


    public List<CodeSubmission> getAllSubmissionsForUser(Long userId){
        logger.info("[CODE_SUBMISSION] Fetching all submissions for user ID: {}", userId);
        try {
            List<CodeSubmission> submissions = codeSubmissionRepository.findByUserId(userId);
            logger.debug("[CODE_SUBMISSION] Retrieved {} submissions for user ID: {}", submissions.size(), userId);
            return submissions;
        } catch (Exception e) {
            logger.error("[CODE_SUBMISSION] Error fetching submissions for user ID: {}", userId, e);
            throw e;
        }
    }

    public List<CodeSubmission> getAllSubmissionsForQuestion(Long questionId){
        logger.info("[CODE_SUBMISSION] Fetching all submissions for question ID: {}", questionId);
        try {
            List<CodeSubmission> submissions = codeSubmissionRepository.findByQuestionId(questionId);
            logger.debug("[CODE_SUBMISSION] Retrieved {} submissions for question ID: {}", submissions.size(), questionId);
            return submissions;
        } catch (Exception e) {
            logger.error("[CODE_SUBMISSION] Error fetching submissions for question ID: {}", questionId, e);
            throw e;
        }
    }

    public Optional<CodeSubmission> getSubmissionById(Long submissionId){
        logger.info("[CODE_SUBMISSION] Fetching submission by ID: {}", submissionId);
        try {
            Optional<CodeSubmission> submission = codeSubmissionRepository.findById(submissionId);
            logger.debug("[CODE_SUBMISSION] Submission fetch completed for ID: {}", submissionId);
            return submission;
        } catch (Exception e) {
            logger.error("[CODE_SUBMISSION] Error fetching submission by ID: {}", submissionId, e);
            throw e;
        }
    }

    public String updateSubmission(Long submissionId, CodeSubmission updatedSubmission) {
        logger.info("[CODE_SUBMISSION] Updating submission ID: {}", submissionId);
        try {
            Optional<CodeSubmission> optionalCodeSubmission = codeSubmissionRepository.findById(submissionId);

            if (optionalCodeSubmission.isPresent()) {
                CodeSubmission oldSubmission = optionalCodeSubmission.get();

                // Update only the fields you want to change
                oldSubmission.setCode(updatedSubmission.getCode());
                oldSubmission.setLanguage(updatedSubmission.getLanguage());
                oldSubmission.setStatus(updatedSubmission.getStatus());
                oldSubmission.setSubmittedAt(updatedSubmission.getSubmittedAt());

                codeSubmissionRepository.save(oldSubmission);
                logger.info("[CODE_SUBMISSION] Submission ID: {} updated successfully", submissionId);
                return "Submission updated Successfully!";
            } else {
                logger.warn("[CODE_SUBMISSION] Submission not found for ID: {}", submissionId);
                return "Submission Not Found!";
            }
        } catch (Exception e) {
            logger.error("[CODE_SUBMISSION] Error updating submission ID: {}", submissionId, e);
            throw e;
        }
    }


    public String deleteSubmissionById(Long submissionId){
        logger.info("[CODE_SUBMISSION] Deleting submission ID: {}", submissionId);
        try {
            Optional<CodeSubmission> optionalCodeSubmission = codeSubmissionRepository.findById(submissionId);

            if(optionalCodeSubmission.isPresent()){
                codeSubmissionRepository.deleteById(submissionId);
                logger.info("[CODE_SUBMISSION] Submission ID: {} deleted successfully", submissionId);
                return "Submission deleted Successfully !";
            }
            else{
                logger.warn("[CODE_SUBMISSION] Submission not found for ID: {}", submissionId);
                return "Submission Not Found !";
            }
        } catch (Exception e) {
            logger.error("[CODE_SUBMISSION] Error deleting submission ID: {}", submissionId, e);
            throw e;
        }
    }

    //Evaluation will be added in future !

}
