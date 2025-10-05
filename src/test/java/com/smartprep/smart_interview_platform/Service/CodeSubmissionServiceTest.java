package com.smartprep.smart_interview_platform.Service;

import com.smartprep.smart_interview_platform.Model.CodeSubmission;
import com.smartprep.smart_interview_platform.Model.Question;
import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Model.UserStats;
import com.smartprep.smart_interview_platform.Repository.CodeSubmissionRepository;
import com.smartprep.smart_interview_platform.Repository.QuestionRepository;
import com.smartprep.smart_interview_platform.Repository.UserRepository;
import com.smartprep.smart_interview_platform.Repository.UserStatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodeSubmissionServiceTest {

    @Mock
    private CodeSubmissionRepository codeSubmissionRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatsRepository userStatsRepository;

    @InjectMocks
    private CodeSubmissionService codeSubmissionService;

    private User sampleUser;
    private Question sampleQuestion;
    private CodeSubmission sampleSubmission;
    private UserStats sampleStats;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("Alice");

        sampleQuestion = new Question(
                1L,
                "Two Sum",
                "Find two numbers that add to target",
                "Easy",
                Arrays.asList("array"),
                "input",
                "output"
        );

        sampleSubmission = new CodeSubmission();
        sampleSubmission.setId(1L);
        sampleSubmission.setUser(sampleUser);
        sampleSubmission.setQuestion(sampleQuestion);
        sampleSubmission.setCode("print('hello')");
        sampleSubmission.setLanguage("Python");
        sampleSubmission.setStatus("Accepted");

        sampleStats = new UserStats();
        sampleStats.setUser(sampleUser);
        sampleStats.setTotalSubmissions(0L);
        sampleStats.setAcceptedSubmissions(0L);
        sampleStats.setQuestionsSolved(0L);
    }

    // ================================
    // submitCode()
    // ================================

    @Test
    void testSubmitCode_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(questionRepository.findById(1L)).thenReturn(Optional.of(sampleQuestion));
        when(codeSubmissionRepository.existsByUserAndQuestionAndStatus(any(), any(), eq("Accepted"))).thenReturn(false);
        when(userStatsRepository.findById(1L)).thenReturn(Optional.of(sampleStats));

        String result = codeSubmissionService.submitCode(sampleSubmission);

        assertTrue(result.contains("Code successfully submitted"));
        verify(codeSubmissionRepository, times(1)).save(any(CodeSubmission.class));
        verify(userStatsRepository, times(1)).save(any(UserStats.class));
    }

    @Test
    void testSubmitCode_MissingUser() {
        sampleSubmission.setUser(null);

        String result = codeSubmissionService.submitCode(sampleSubmission);

        assertEquals("User is missing or User ID is null!", result);
        verifyNoInteractions(userRepository, questionRepository);
    }

    @Test
    void testSubmitCode_InvalidUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        String result = codeSubmissionService.submitCode(sampleSubmission);

        assertEquals("Invalid User ID or User not found!", result);
    }

    @Test
    void testSubmitCode_InvalidQuestion() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        String result = codeSubmissionService.submitCode(sampleSubmission);

        assertEquals("Invalid Question ID or Question not found!", result);
    }

    // ================================
    // getAllSubmissionsForUser()
    // ================================

    @Test
    void testGetAllSubmissionsForUser() {
        when(codeSubmissionRepository.findByUserId(1L)).thenReturn(Arrays.asList(sampleSubmission));

        List<CodeSubmission> result = codeSubmissionService.getAllSubmissionsForUser(1L);

        assertEquals(1, result.size());
    }

    // ================================
    // getAllSubmissionsForQuestion()
    // ================================

    @Test
    void testGetAllSubmissionsForQuestion() {
        when(codeSubmissionRepository.findByQuestionId(1L)).thenReturn(Arrays.asList(sampleSubmission));

        List<CodeSubmission> result = codeSubmissionService.getAllSubmissionsForQuestion(1L);

        assertEquals(1, result.size());
    }

    // ================================
    // getSubmissionById()
    // ================================

    @Test
    void testGetSubmissionById() {
        when(codeSubmissionRepository.findById(1L)).thenReturn(Optional.of(sampleSubmission));

        Optional<CodeSubmission> result = codeSubmissionService.getSubmissionById(1L);

        assertTrue(result.isPresent());
    }

    // ================================
    // updateSubmission()
    // ================================

    @Test
    void testUpdateSubmission_Success() {
        CodeSubmission updated = new CodeSubmission();
        updated.setCode("print('updated')");
        updated.setLanguage("Java");
        updated.setStatus("Wrong Answer");
        updated.setSubmittedAt(LocalDateTime.now());

        when(codeSubmissionRepository.findById(1L)).thenReturn(Optional.of(sampleSubmission));

        String result = codeSubmissionService.updateSubmission(1L, updated);

        assertEquals("Submission updated Successfully!", result);
        verify(codeSubmissionRepository, times(1)).save(any(CodeSubmission.class));
    }

    @Test
    void testUpdateSubmission_NotFound() {
        when(codeSubmissionRepository.findById(99L)).thenReturn(Optional.empty());

        String result = codeSubmissionService.updateSubmission(99L, sampleSubmission);

        assertEquals("Submission Not Found!", result);
    }

    // ================================
    // deleteSubmissionById()
    // ================================

    @Test
    void testDeleteSubmissionById_Success() {
        when(codeSubmissionRepository.findById(1L)).thenReturn(Optional.of(sampleSubmission));

        String result = codeSubmissionService.deleteSubmissionById(1L);

        assertEquals("Submission deleted Successfully !", result);
        verify(codeSubmissionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteSubmissionById_NotFound() {
        when(codeSubmissionRepository.findById(99L)).thenReturn(Optional.empty());

        String result = codeSubmissionService.deleteSubmissionById(99L);

        assertEquals("Submission Not Found !", result);
    }
}
