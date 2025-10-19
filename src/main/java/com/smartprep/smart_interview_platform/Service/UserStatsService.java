    package com.smartprep.smart_interview_platform.Service;

    import java.util.Comparator;
    import java.util.List;
    import java.util.stream.Collectors;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import com.smartprep.smart_interview_platform.Model.LeaderBoard;
    import com.smartprep.smart_interview_platform.Model.UserStats;
    import com.smartprep.smart_interview_platform.Repository.UserStatsRepository;

    @Service
    public class UserStatsService {

        private static final Logger logger = LoggerFactory.getLogger(UserStatsService.class);

        @Autowired
        private UserStatsRepository userStatsRepository;


        public List<LeaderBoard> getLeaderBoard(String leaderBoardType){
            logger.info("[STATS_SERVICE] Fetching leaderboard of type: {}", leaderBoardType);
            try {
                List<UserStats> allStats = userStatsRepository.findAll();

                if (allStats.isEmpty()) {
                    logger.warn("[STATS_SERVICE] No user stats found for leaderboard type: {}", leaderBoardType);
                    return List.of();
                }

                Comparator<LeaderBoard> comparator;

                switch (leaderBoardType){
                    case "questionsSolved":
                        logger.debug("[STATS_SERVICE] Sorting leaderboard by questions solved");
                        comparator = Comparator.comparingLong(LeaderBoard::getQuestionsSolved).reversed();
                        break;
                    case "acceptedQuestions":
                        logger.debug("[STATS_SERVICE] Sorting leaderboard by accepted submissions");
                        comparator = Comparator.comparingLong(LeaderBoard::getAcceptedSubmissions).reversed();
                        break;
                    case "totalSubmissions":
                        logger.debug("[STATS_SERVICE] Sorting leaderboard by total submissions");
                        comparator = Comparator.comparingLong(LeaderBoard::getTotalSubmissions).reversed();
                        break;
                    case "accuracy":
                        logger.debug("[STATS_SERVICE] Sorting leaderboard by accuracy");
                        comparator = Comparator.comparingDouble(LeaderBoard::getAccuracy).reversed();
                        break;
                    default:
                        logger.debug("[STATS_SERVICE] Unknown leaderboard type: {}, defaulting to questions solved", leaderBoardType);
                        comparator = Comparator.comparingLong(LeaderBoard::getQuestionsSolved).reversed();
                        break;
                }

                List<LeaderBoard> leaderboard = allStats.stream()
                        .map(s -> new LeaderBoard(
                                s.getUserId(),
                                s.getUser().getName(),
                                s.getQuestionsSolved(),
                                s.getAcceptedSubmissions(),
                                s.getTotalSubmissions(),
                                s.getAccuracy()
                        ))
                        .sorted(comparator)
                        .collect(Collectors.toList());

                logger.info("[STATS_SERVICE] Leaderboard generated with {} entries for type: {}", leaderboard.size(), leaderBoardType);
                return leaderboard;
            } catch (Exception e) {
                logger.error("[STATS_SERVICE] Error fetching leaderboard of type: {}", leaderBoardType, e);
                throw e;
            }
        }

    }
