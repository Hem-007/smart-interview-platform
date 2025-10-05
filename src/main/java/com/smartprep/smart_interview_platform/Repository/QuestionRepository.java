package com.smartprep.smart_interview_platform.Repository;

import com.smartprep.smart_interview_platform.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository  extends JpaRepository<Question,Long> {
    // Search by title or description
    List<Question> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    // Search by tag (MySQL join works with ElementCollection)
    @Query("SELECT q FROM Question q JOIN q.tags t WHERE LOWER(t) LIKE LOWER(CONCAT('%', :tag, '%'))")
    List<Question> findByTag(@Param("tag") String tag);

}
