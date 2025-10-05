package com.smartprep.smart_interview_platform.Service;

import com.smartprep.smart_interview_platform.Model.CodeSubmission;
import com.smartprep.smart_interview_platform.Model.Question;
import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Model.UserStats;
import com.smartprep.smart_interview_platform.Repository.CodeSubmissionRepository;
import com.smartprep.smart_interview_platform.Repository.QuestionRepository;
import com.smartprep.smart_interview_platform.Repository.UserRepository;
import com.smartprep.smart_interview_platform.Repository.UserStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CodeSubmissionService {
    @Autowired
    private CodeSubmissionRepository codeSubmissionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatsRepository userStatsRepository;

    public String submitCode(CodeSubmission codeSubmission) {
        UserStats userStats = new UserStats();
        User user = new User();
        if (codeSubmission.getUser() == null || codeSubmission.getUser().getId() == null) {
            return "User is missing or User ID is null!";
        }

        if (codeSubmission.getQuestion() == null || codeSubmission.getQuestion().getId() == null) {
            return "Question is missing or Question ID is null!";
        }

        Optional<User> optionalUser = userRepository.findById(codeSubmission.getUser().getId());
        if (optionalUser.isEmpty()) {
            return "Invalid User ID or User not found!";
        }

        Optional<Question> optionalQuestion = questionRepository.findById(codeSubmission.getQuestion().getId());
        if (optionalQuestion.isEmpty()) {
            return "Invalid Question ID or Question not found!";
        }

        // Set the actual User and Question from DB to avoid transient object errors
        codeSubmission.setUser(optionalUser.get());
        codeSubmission.setQuestion(optionalQuestion.get());
        codeSubmission.setSubmittedAt(LocalDateTime.now());
        boolean firstSolve = codeSubmissionRepository.existsByUserAndQuestionAndStatus(codeSubmission.getUser(),codeSubmission.getQuestion(),"Accepted");

        codeSubmissionRepository.save(codeSubmission);

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
        }

        //Setting the Questions Solved
        System.out.println("✅✅"+firstSolve+"✅✅");
        if(!firstSolve){
            System.out.println("This question is First solve ✅ ");
            stats.setQuestionsSolved(stats.getQuestionsSolved()+1);
        }


        Double accuracy = (stats.getAcceptedSubmissions() * 100.0 ) / stats.getTotalSubmissions();
        stats.setAccuracy(accuracy);

        userStatsRepository.save(stats);

        return "✅ Code successfully submitted by " + optionalUser.get().getName() +
                " for the question \"" + optionalQuestion.get().getTitle() + "\"";
    }


    public List<CodeSubmission> getAllSubmissionsForUser(Long userId){
        return codeSubmissionRepository.findByUserId(userId);
    }

    public List<CodeSubmission> getAllSubmissionsForQuestion(Long questionId){
        return codeSubmissionRepository.findByQuestionId(questionId);
    }

    public Optional<CodeSubmission> getSubmissionById(Long submissionId){
        return codeSubmissionRepository.findById(submissionId);
    }

    public String updateSubmission(Long submissionId, CodeSubmission updatedSubmission) {
        Optional<CodeSubmission> optionalCodeSubmission = codeSubmissionRepository.findById(submissionId);

        if (optionalCodeSubmission.isPresent()) {
            CodeSubmission oldSubmission = optionalCodeSubmission.get();

            // Update only the fields you want to change
            oldSubmission.setCode(updatedSubmission.getCode());
            oldSubmission.setLanguage(updatedSubmission.getLanguage());
            oldSubmission.setStatus(updatedSubmission.getStatus());
            oldSubmission.setSubmittedAt(updatedSubmission.getSubmittedAt());

            codeSubmissionRepository.save(oldSubmission);
            return "Submission updated Successfully!";
        } else {
            return "Submission Not Found!";
        }
    }


    public String deleteSubmissionById(Long submissionId){
        Optional<CodeSubmission> optionalCodeSubmission = codeSubmissionRepository.findById(submissionId);

        if(optionalCodeSubmission.isPresent()){
            codeSubmissionRepository.deleteById(submissionId);
            return "Submission deleted Successfully !";
        }
        else{
            return "Submission Not Found !";
        }
    }

    //Evaluation will be added in future !

}
