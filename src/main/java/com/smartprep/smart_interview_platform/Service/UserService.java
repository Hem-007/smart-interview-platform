package com.smartprep.smart_interview_platform.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Model.UserStats;
import com.smartprep.smart_interview_platform.Repository.UserRepository;
import com.smartprep.smart_interview_platform.Repository.UserStatsRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatsRepository userStatsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public String register(User user){
        logger.info("[USER_SERVICE] User registration initiated for email: {}", user.getEmail());
        try {
            Optional<User> existing = userRepository.findByEmail(user.getEmail());
            if(existing.isPresent()){
                logger.warn("[USER_SERVICE] Registration failed - User already exists with email: {}", user.getEmail());
                throw new RuntimeException("User Already Exists !");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("ROLE_USER");
            userRepository.save(user);
            logger.debug("[USER_SERVICE] User saved to database: {}", user.getEmail());

            UserStats userStats = new UserStats();
            userStats.setUser(user);
            userStats.setQuestionsSolved(0L);
            userStats.setAcceptedSubmissions(0L);
            userStats.setTotalSubmissions(0L);
            userStats.setAccuracy(0.0);
            userStatsRepository.save(userStats);
            logger.debug("[USER_SERVICE] User stats initialized for email: {}", user.getEmail());

            logger.info("[USER_SERVICE] User registration completed successfully for email: {}", user.getEmail());
            return "Registration Successful";
        } catch (Exception e) {
            logger.error("[USER_SERVICE] Error during user registration for email: {}", user.getEmail(), e);
            throw e;
        }
    }

    public ResponseEntity<String> login(String email, String rawPassword){
        logger.info("[USER_SERVICE] Login attempt for email: {}", email);
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if(userOptional.isPresent()){
                User user = userOptional.get();
                if(passwordEncoder.matches(rawPassword, user.getPassword())){
                    logger.info("[USER_SERVICE] Login successful for email: {}", email);
                    return ResponseEntity.ok("Login Successful!");
                }
                else {
                    logger.warn("[USER_SERVICE] Login failed - Incorrect password for email: {}", email);
                    throw new RuntimeException("Incorrect Password");
                }
            }
            logger.warn("[USER_SERVICE] Login failed - User not found for email: {}", email);
            throw new RuntimeException("User Not Found !");
        } catch (Exception e) {
            logger.error("[USER_SERVICE] Error during login for email: {}", email, e);
            throw e;
        }
    }

    public ResponseEntity<User> getUser(String email){
        logger.info("[USER_SERVICE] Fetching user by email: {}", email);
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if(optionalUser.isPresent()){
                logger.debug("[USER_SERVICE] User found for email: {}", email);
                return ResponseEntity.ok(optionalUser.get());
            }
            else{
                logger.warn("[USER_SERVICE] User not found for email: {}", email);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("[USER_SERVICE] Error fetching user by email: {}", email, e);
            throw e;
        }
    }

    public List<User> getAllUsers(){
        logger.info("[USER_SERVICE] Fetching all users");
        try {
            List<User> users = userRepository.findAll();
            logger.debug("[USER_SERVICE] Retrieved {} users", users.size());
            return users;
        } catch (Exception e) {
            logger.error("[USER_SERVICE] Error fetching all users", e);
            throw e;
        }
    }


    public Optional<User> getUserById(Long id){
        logger.info("[USER_SERVICE] Fetching user by ID: {}", id);
        try {
            Optional<User> user = userRepository.findById(id);
            logger.debug("[USER_SERVICE] User fetch completed for ID: {}", id);
            return user;
        } catch (Exception e) {
            logger.error("[USER_SERVICE] Error fetching user by ID: {}", id, e);
            throw e;
        }
    }

    public Optional<User> getUserByEmail(String email){
        logger.info("[USER_SERVICE] Fetching user by email: {}", email);
        try {
            Optional<User> user = userRepository.findByEmail(email);
            logger.debug("[USER_SERVICE] User fetch completed for email: {}", email);
            return user;
        } catch (Exception e) {
            logger.error("[USER_SERVICE] Error fetching user by email: {}", email, e);
            throw e;
        }
    }

}
