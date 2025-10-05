package com.smartprep.smart_interview_platform.Controller;

import com.smartprep.smart_interview_platform.Model.LeaderBoard;
import com.smartprep.smart_interview_platform.Service.UserStatsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserStatsService userStatsService;

    @Test
    void testGetLeaderBoard_ReturnsData() throws Exception {
        // Arrange
        LeaderBoard lb = new LeaderBoard(1L, "John Doe", 10L, 8L, 12L, 66.7);
        Mockito.when(userStatsService.getLeaderBoard("questionsSolved"))
                .thenReturn(List.of(lb));

        // Act & Assert
        mockMvc.perform(get("/leaderboard/questionsSolved")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userName").value("John Doe"))
                .andExpect(jsonPath("$[0].questionsSolved").value(10))
                .andExpect(jsonPath("$[0].acceptedSubmissions").value(8))
                .andExpect(jsonPath("$[0].totalSubmissions").value(12))
                .andExpect(jsonPath("$[0].accuracy").value(66.7));
    }

    @Test
    void testGetLeaderBoard_ReturnsNotFound() throws Exception {
        // Arrange
        Mockito.when(userStatsService.getLeaderBoard("accuracy"))
                .thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/leaderboard/accuracy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("error: No leaderboard data found on Questions Solved"));
    }
}
