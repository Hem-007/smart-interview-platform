package com.smartprep.smart_interview_platform.Controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartprep.smart_interview_platform.Model.LeaderBoard;
import com.smartprep.smart_interview_platform.Service.UserStatsService;

@RestController
@RequestMapping("/leaderboard")
public class UserStatsController {

    private static final Logger logger = LoggerFactory.getLogger(UserStatsController.class);

    @Autowired
    private UserStatsService userStatsService;

    @GetMapping("/{leaderBoardType}")
    public ResponseEntity<?> getLeaderBoardByAccuracy(@PathVariable String leaderBoardType){
        logger.info("[LEADERBOARD] Fetching leaderboard of type: {}", leaderBoardType);
        try {
            List<LeaderBoard> leaderboard = userStatsService.getLeaderBoard(leaderBoardType);
            if (leaderboard == null || leaderboard.isEmpty()) {
                logger.warn("[LEADERBOARD] No leaderboard data found for type: {}", leaderBoardType);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("error: No leaderboard data found on Questions Solved");
            } else {
                logger.debug("[LEADERBOARD] Retrieved {} entries for leaderboard type: {}", leaderboard.size(), leaderBoardType);
                return ResponseEntity.ok(leaderboard);
            }
        } catch (Exception e) {
            logger.error("[LEADERBOARD] Error fetching leaderboard of type: {}", leaderBoardType, e);
            throw e;
        }
    }


}
