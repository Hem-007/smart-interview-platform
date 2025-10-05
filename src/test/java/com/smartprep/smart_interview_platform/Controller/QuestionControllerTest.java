package com.smartprep.smart_interview_platform.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartprep.smart_interview_platform.Model.Question;
import com.smartprep.smart_interview_platform.Service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private QuestionService questionService;

    @Test
    void testCreateQuestion() throws Exception {
        Question question = new Question(1L, "Two Sum", "Find two numbers", "Easy",
                Arrays.asList("array", "math"), "1 2", "3");

        when(questionService.createQuestion(any(Question.class)))
                .thenReturn("Question Created Successfully!");

        mockMvc.perform(post("/questions/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isOk())
                .andExpect(content().string("Question Created Successfully!"));
    }

    @Test
    void testGetAllQuestions() throws Exception {
        Question q1 = new Question(1L, "Two Sum", "Desc1", "Easy", Arrays.asList("array"), "input1", "output1");
        Question q2 = new Question(2L, "Three Sum", "Desc2", "Medium", Arrays.asList("math"), "input2", "output2");
        List<Question> questions = Arrays.asList(q1, q2);

        when(questionService.getAllQuestions()).thenReturn(questions);

        mockMvc.perform(get("/questions/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Two Sum"));
    }

    @Test
    void testGetQuestionById() throws Exception {
        Question question = new Question(1L, "Two Sum", "Desc", "Easy", Arrays.asList("array"), "input", "output");

        when(questionService.getQuestionById(1L)).thenReturn(question);

        mockMvc.perform(get("/questions/question/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Two Sum"));
    }

    @Test
    void testUpdateQuestion() throws Exception {
        Question updatedQuestion = new Question(1L, "Updated Title", "Updated Desc", "Medium",
                Arrays.asList("updated"), "new input", "new output");

        when(questionService.updateQuestion(any(Long.class), any(Question.class)))
                .thenReturn("Question Updated Successfully !");

        mockMvc.perform(put("/questions/question/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedQuestion)))
                .andExpect(status().isOk())
                .andExpect(content().string("Question Updated Successfully !"));
    }

    @Test
    void testDeleteQuestionById() throws Exception {
        when(questionService.deleteQuestionById(1L))
                .thenReturn("Question Deleted Successfully !");

        mockMvc.perform(delete("/questions/question/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Question Deleted Successfully !"));
    }

    @Test
    void testSearchQuestions() throws Exception {
        Question q = new Question(1L, "Two Sum", "Desc", "Easy", Arrays.asList("array"), "input", "output");

        when(questionService.searchQuestions("Two")).thenReturn(Arrays.asList(q));

        mockMvc.perform(get("/questions/search")
                        .param("keyword", "Two"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Two Sum"));
    }

    @Test
    void testSearchByTag() throws Exception {
        Question q = new Question(1L, "Two Sum", "Desc", "Easy", Arrays.asList("array"), "input", "output");

        when(questionService.searchByTag("array")).thenReturn(Arrays.asList(q));

        mockMvc.perform(get("/questions/searchByTag")
                        .param("tag", "array"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].tags[0]").value("array"));
    }
}
