package com.smartprep.smart_interview_platform.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartprep.smart_interview_platform.Model.CodeSubmission;
import com.smartprep.smart_interview_platform.Model.Question;
import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Service.CodeSubmissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CodeSubmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CodeSubmissionService codeSubmissionService;

    private User getMockUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("encodedpass");
        user.setRole("user");
        return user;
    }

    private Question getMockQuestion() {
        Question q = new Question();
        q.setId(1L);
        q.setTitle("Two Sum");
        q.setDescription("Find indices of two numbers that add up to target");
        q.setDifficulty("Easy");
        q.setTags(Collections.singletonList("Array, HashMap"));
        return q;
    }

    private CodeSubmission getMockSubmission() {
        CodeSubmission submission = new CodeSubmission();
        submission.setId(1L);
        submission.setCode("System.out.println(\"Hello\");");
        submission.setStatus("Success");
        submission.setLanguage("Java");
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setUser(getMockUser());
        submission.setQuestion(getMockQuestion());
        return submission;
    }

    @Test
    void testSubmitCode() throws Exception {
        CodeSubmission submission = getMockSubmission();

        when(codeSubmissionService.submitCode(any(CodeSubmission.class)))
                .thenReturn("Code Submitted Successfully!");

        mockMvc.perform(post("/submission/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submission)))
                .andExpect(status().isOk())
                .andExpect(content().string("Code Submitted Successfully!"));
    }

    @Test
    void testGetAllSubmissionsForUser() throws Exception {
        CodeSubmission s1 = getMockSubmission();
        CodeSubmission s2 = getMockSubmission();
        s2.setId(2L);
        s2.setStatus("Fail");

        List<CodeSubmission> submissions = Arrays.asList(s1, s2);

        when(codeSubmissionService.getAllSubmissionsForUser(1L)).thenReturn(submissions);

        mockMvc.perform(get("/submission/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].user.email").value("john@example.com"))
                .andExpect(jsonPath("$[1].status").value("Fail"));
    }

    @Test
    void testGetAllSubmissionsForQuestion() throws Exception {
        CodeSubmission s1 = getMockSubmission();

        when(codeSubmissionService.getAllSubmissionsForQuestion(1L)).thenReturn(Arrays.asList(s1));

        mockMvc.perform(get("/submission/question/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].question.title").value("Two Sum"));
    }

    @Test
    void testGetSubmissionById() throws Exception {
        CodeSubmission submission = getMockSubmission();

        when(codeSubmissionService.getSubmissionById(1L)).thenReturn(Optional.of(submission));

        mockMvc.perform(get("/submission/submission/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("System.out.println(\"Hello\");"))
                .andExpect(jsonPath("$.user.name").value("John"))
                .andExpect(jsonPath("$.question.title").value("Two Sum"));
    }

    @Test
    void testUpdateSubmission() throws Exception {
        CodeSubmission updated = getMockSubmission();
        updated.setStatus("Updated");

        when(codeSubmissionService.updateSubmission(any(Long.class), any(CodeSubmission.class)))
                .thenReturn("Submission Updated Successfully!");

        mockMvc.perform(put("/submission/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(content().string("Submission Updated Successfully!"));
    }

    @Test
    void testDeleteSubmissionById() throws Exception {
        when(codeSubmissionService.deleteSubmissionById(1L))
                .thenReturn("Submission Deleted Successfully!");

        mockMvc.perform(delete("/submission/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Submission Deleted Successfully!"));
    }
}
