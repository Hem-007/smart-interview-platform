package com.smartprep.smart_interview_platform.Service;

import com.smartprep.smart_interview_platform.Model.LeaderBoard;
import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Model.UserStats;
import com.smartprep.smart_interview_platform.Repository.UserStatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserStatsServiceTest {

    @Mock
    private UserStatsRepository userStatsRepository;

    @InjectMocks
    private UserStatsService userStatsService;

    private UserStats user1Stats;
    private UserStats user2Stats;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");

        user1Stats = new UserStats();
        user1Stats.setUserId(1L);
        user1Stats.setUser(user1);
        user1Stats.setQuestionsSolved(10L);
        user1Stats.setAcceptedSubmissions(8L);
        user1Stats.setTotalSubmissions(12L);
        user1Stats.setAccuracy(0.8);

        user2Stats = new UserStats();
        user2Stats.setUserId(2L);
        user2Stats.setUser(user2);
        user2Stats.setQuestionsSolved(15L);
        user2Stats.setAcceptedSubmissions(12L);
        user2Stats.setTotalSubmissions(20L);
        user2Stats.setAccuracy(0.6);
    }

    @Test
    void testGetLeaderBoard_EmptyList() {
        when(userStatsRepository.findAll()).thenReturn(Collections.emptyList());

        List<LeaderBoard> result = userStatsService.getLeaderBoard("questionsSolved");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLeaderBoard_ByQuestionsSolved() {
        when(userStatsRepository.findAll()).thenReturn(Arrays.asList(user1Stats, user2Stats));

        List<LeaderBoard> result = userStatsService.getLeaderBoard("questionsSolved");

        assertEquals(2, result.size());
        assertEquals("Bob", result.get(0).getUserName()); // Bob has 15 solved > Alice 10
    }

    @Test
    void testGetLeaderBoard_ByAcceptedSubmissions() {
        when(userStatsRepository.findAll()).thenReturn(Arrays.asList(user1Stats, user2Stats));

        List<LeaderBoard> result = userStatsService.getLeaderBoard("acceptedQuestions");

        assertEquals(2, result.size());
        assertEquals("Bob", result.get(0).getUserName()); // Bob 12 > Alice 8
    }

    @Test
    void testGetLeaderBoard_ByTotalSubmissions() {
        when(userStatsRepository.findAll()).thenReturn(Arrays.asList(user1Stats, user2Stats));

        List<LeaderBoard> result = userStatsService.getLeaderBoard("totalSubmissions");

        assertEquals(2, result.size());
        assertEquals("Bob", result.get(0).getUserName()); // Bob 20 > Alice 12
    }

    @Test
    void testGetLeaderBoard_ByAccuracy() {
        when(userStatsRepository.findAll()).thenReturn(Arrays.asList(user1Stats, user2Stats));

        List<LeaderBoard> result = userStatsService.getLeaderBoard("accuracy");

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getUserName()); // Alice 0.8 > Bob 0.6
    }

    @Test
    void testGetLeaderBoard_DefaultCase() {
        when(userStatsRepository.findAll()).thenReturn(Arrays.asList(user1Stats, user2Stats));

        List<LeaderBoard> result = userStatsService.getLeaderBoard("unknownType");

        assertEquals(2, result.size());
        assertEquals("Bob", result.get(0).getUserName()); // Falls back to questionsSolved
    }
}
