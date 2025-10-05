    package com.smartprep.smart_interview_platform.Service;

    import com.smartprep.smart_interview_platform.Model.LeaderBoard;
    import com.smartprep.smart_interview_platform.Model.UserStats;
    import com.smartprep.smart_interview_platform.Repository.UserStatsRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.Comparator;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class UserStatsService {

        @Autowired
        private UserStatsRepository userStatsRepository;


        public List<LeaderBoard> getLeaderBoard(String leaderBoardType){

            List<UserStats> allStats = userStatsRepository.findAll();

            if (allStats.isEmpty()) {
                return List.of();
            }

            Comparator<LeaderBoard> comparator;

            switch (leaderBoardType){
                case "questionsSolved":
                    comparator = Comparator.comparingLong(LeaderBoard::getQuestionsSolved).reversed();
                    break;
                case "acceptedQuestions":
                    comparator = Comparator.comparingLong(LeaderBoard::getAcceptedSubmissions).reversed();
                    break;
                case "totalSubmissions":
                    comparator = Comparator.comparingLong(LeaderBoard::getTotalSubmissions).reversed();
                    break;
                case "accuracy":
                    comparator = Comparator.comparingDouble(LeaderBoard::getAccuracy).reversed();
                    break;
                default:
                    comparator = Comparator.comparingLong(LeaderBoard::getQuestionsSolved).reversed();
                    break;
            }

            return allStats.stream()
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

        }

    }
