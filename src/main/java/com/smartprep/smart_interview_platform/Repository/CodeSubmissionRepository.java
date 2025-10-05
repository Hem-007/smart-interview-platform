package com.smartprep.smart_interview_platform.Repository;

import com.smartprep.smart_interview_platform.Model.CodeSubmission;
import com.smartprep.smart_interview_platform.Model.Question;
import com.smartprep.smart_interview_platform.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission , Long> {

    public List<CodeSubmission> findByUserId(Long userId);
    public List<CodeSubmission> findByQuestionId(Long userId);
    public boolean existsByUserAndQuestionAndStatus(User user, Question question, String status);

}
