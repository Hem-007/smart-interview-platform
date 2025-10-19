package com.smartprep.smart_interview_platform.Controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartprep.smart_interview_platform.Model.EmailRequest;
import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Service.UserService;
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

//    @PostMapping("/login")
//    public ResponseEntity<String> login (@RequestBody LoginRequest loginRequest){
//        ResponseEntity<String> result = userService.login(loginRequest.getEmail(),loginRequest.getPassword());
//        return  result;
//    }

    @PostMapping("/users")
    public ResponseEntity<User> getUser(@RequestBody EmailRequest emailRequest){
        logger.info("[USER] Fetching user by email: {}", emailRequest.getEmail());
        try {
            ResponseEntity<User> result = userService.getUser(emailRequest.getEmail());
            logger.debug("[USER] User fetch completed for email: {}", emailRequest.getEmail());
            return result;
        } catch (Exception e) {
            logger.error("[USER] Error fetching user by email: {}", emailRequest.getEmail(), e);
            throw e;
        }
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        logger.info("[USER] Fetching all users");
        try {
            List<User> users = userService.getAllUsers();
            logger.debug("[USER] Retrieved {} users", users.size());
            return users;
        } catch (Exception e) {
            logger.error("[USER] Error fetching all users", e);
            throw e;
        }
    }

    @GetMapping("/id/{id}")
    public Optional<User> getUserById(@PathVariable Long id){
        logger.info("[USER] Fetching user by ID: {}", id);
        try {
            Optional<User> user = userService.getUserById(id);
            logger.debug("[USER] User fetch completed for ID: {}", id);
            return user;
        } catch (Exception e) {
            logger.error("[USER] Error fetching user by ID: {}", id, e);
            throw e;
        }
    }

    @GetMapping("/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email){
        logger.info("[USER] Fetching user by email: {}", email);
        try {
            Optional<User> user = userService.getUserByEmail(email);
            logger.debug("[USER] User fetch completed for email: {}", email);
            return user;
        } catch (Exception e) {
            logger.error("[USER] Error fetching user by email: {}", email, e);
            throw e;
        }
    }

}
