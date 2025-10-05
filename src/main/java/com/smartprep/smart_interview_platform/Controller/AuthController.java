package com.smartprep.smart_interview_platform.Controller;

import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Security.JwtUtil;
import com.smartprep.smart_interview_platform.Service.CustomUserDetailsService;
import com.smartprep.smart_interview_platform.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class AuthController {

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
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        if (authentication.isAuthenticated()) {
            var userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            String token = jwtUtil.generateToken(user.getEmail(), role);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }
}
