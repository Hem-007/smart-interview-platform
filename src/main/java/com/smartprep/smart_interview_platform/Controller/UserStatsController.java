package com.smartprep.smart_interview_platform.Controller;


import com.smartprep.smart_interview_platform.Model.LeaderBoard;
import com.smartprep.smart_interview_platform.Repository.UserStatsRepository;
import com.smartprep.smart_interview_platform.Service.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class UserStatsController {

    @Autowired
    private UserStatsService userStatsService;
    @GetMapping("/{leaderBoardType}")
    public ResponseEntity<?> getLeaderBoardByAccuracy(@PathVariable String leaderBoardType){

        List<LeaderBoard> leaderboard = userStatsService.getLeaderBoard(leaderBoardType);
        if (leaderboard == null || leaderboard.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("error: No leaderboard data found on Questions Solved");
        } else {
            return ResponseEntity.ok(leaderboard);
        }

    }


}
