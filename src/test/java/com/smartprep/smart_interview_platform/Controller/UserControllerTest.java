package com.smartprep.smart_interview_platform.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartprep.smart_interview_platform.Model.EmailRequest;
import com.smartprep.smart_interview_platform.Model.LoginRequest;
import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    // --- Register ---
    @Test
    void testRegister() throws Exception {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("password");

        when(userService.register(any(User.class)))
                .thenReturn("Registration Successful");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Registration Successful"));
    }

    // --- Login ---
    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("john@example.com", "password");

        when(userService.login("john@example.com", "password"))
                .thenReturn(ResponseEntity.ok("Login Successful!")); // matches service return type

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login Successful!"));
    }

    // --- Get User by Email (POST /users) ---
    @Test
    void testGetUserByEmailRequest() throws Exception {
        EmailRequest emailRequest = new EmailRequest("john@example.com");
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@example.com");

        when(userService.getUser("john@example.com"))
                .thenReturn(ResponseEntity.ok(user));

        mockMvc.perform(post("/auth/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.name").value("John"));
    }

    // --- Get All Users ---
    @Test
    void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(
                new User(1L, "John", "john@example.com", "password"),
                new User(2L, "Alice", "alice@example.com", "secret")
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/auth/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    // --- Get User by ID ---
    @Test
    void testGetUserById() throws Exception {
        User user = new User(1L, "John", "john@example.com", "password");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/auth/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    // --- Get User by Email (GET /auth/email/{email}) ---
    @Test
    void testGetUserByEmailPath() throws Exception {
        User user = new User(1L, "John", "john@example.com", "password");

        when(userService.getUserByEmail("john@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/auth/email/john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.name").value("John"));
    }
}
