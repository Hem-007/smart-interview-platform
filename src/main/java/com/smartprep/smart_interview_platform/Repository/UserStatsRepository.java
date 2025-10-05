package com.smartprep.smart_interview_platform.Repository;

import com.smartprep.smart_interview_platform.Model.LeaderBoard;
import com.smartprep.smart_interview_platform.Model.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserStatsRepository extends JpaRepository<UserStats , Long> {
}
