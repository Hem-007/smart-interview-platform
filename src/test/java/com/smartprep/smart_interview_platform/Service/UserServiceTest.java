package com.smartprep.smart_interview_platform.Service;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Model.UserStats;
import com.smartprep.smart_interview_platform.Repository.UserRepository;
import com.smartprep.smart_interview_platform.Repository.UserStatsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatsRepository userStatsRepository;

    @InjectMocks
    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void testGetUserById_Success(){

        User user = new User(1L,"user","user@gmail.com","user@123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("user",result.get().getName());
    }

    @Test
    void testGetUserById_NotFound(){
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUserByEmail_Success(){

        User user = new User(1L,"user","user@gmail.com","user@123");

        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("user@gmail.com");

        assertNotNull(result);
        assertEquals("user@gmail.com",result.get().getEmail());
    }
    @Test
    void testGetUserByEmail_NotFound(){
        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail("user@gmail.com");

        assertTrue(result.isEmpty());
    }


    @Test
    void testRegister_Success(){
        User user = new User(null,"user","user@gmail.com","user@123");
        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        when(userStatsRepository.save(any(UserStats.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = userService.register(user);

        assertNotNull(result);
        assertEquals("Registration Successful", result);

        verify(userRepository ,times(1)).findByEmail("user@gmail.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(userStatsRepository, times(1)).save(any(UserStats.class));
    }

    @Test
    void testRegister_Failure_DuplicateMail(){
        User user = new User(1L,"user","user@gmail.com","user@123");

        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,  () -> userService.register(user));

    }


    @Test
    void testLogin_Success(){

        String rawPassword = "user@123";

//        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User(1L,"user","user@gmail.com",passwordEncoder.encode(rawPassword));

        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));

        ResponseEntity<String> loggedIn = userService.login("user@gmail.com", "user@123");

        assertNotNull(loggedIn);
        assertEquals("Login Successful!", loggedIn.getBody());
    }


    @Test
    void testLogin_Failure_WrongPassword(){
        User user = new User(1L,"user","user@gmail.com",passwordEncoder.encode("user@123"));

        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class , () -> userService.login("user@gmail.com", passwordEncoder.encode("wrongPass")));

    }

    @Test
    void testLogin_Failure_UserNotFound(){
        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class , () -> userService.login("user@gmail.com", "user@123"));
    }

}
