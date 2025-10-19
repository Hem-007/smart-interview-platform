package com.smartprep.smart_interview_platform.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Security.JwtUtil;
import com.smartprep.smart_interview_platform.Service.CustomUserDetailsService;
import com.smartprep.smart_interview_platform.Service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        logger.info("[AUTH] User registration attempt for email: {}", user.getEmail());
        try {
            String result = userService.register(user);
            logger.info("[AUTH] User registration successful for email: {}", user.getEmail());
            return result;
        } catch (Exception e) {
            logger.error("[AUTH] User registration failed for email: {}", user.getEmail(), e);
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        logger.info("[AUTH] Login attempt for email: {}", user.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                var userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
                String role = userDetails.getAuthorities().iterator().next().getAuthority();
                String token = jwtUtil.generateToken(user.getEmail(), role);
                logger.info("[AUTH] Login successful for email: {} with role: {}", user.getEmail(), role);
                return ResponseEntity.ok(token);
            } else {
                logger.warn("[AUTH] Login failed - Invalid credentials for email: {}", user.getEmail());
                return ResponseEntity.status(401).body("Invalid Credentials");
            }
        } catch (Exception e) {
            logger.error("[AUTH] Login error for email: {}", user.getEmail(), e);
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }
}
