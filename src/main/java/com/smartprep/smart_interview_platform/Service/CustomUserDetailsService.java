package com.smartprep.smart_interview_platform.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smartprep.smart_interview_platform.Model.CustomUserPrinciple;
import com.smartprep.smart_interview_platform.Model.User;
import com.smartprep.smart_interview_platform.Repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("[AUTH_SERVICE] Loading user details for email: {}", email);
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> {
                logger.warn("[AUTH_SERVICE] User not found with email: {}", email);
                return new UsernameNotFoundException("User not found with email: " + email);
            });
            logger.debug("[AUTH_SERVICE] User details loaded successfully for email: {}", email);
            return new CustomUserPrinciple(user);
        } catch (UsernameNotFoundException e) {
            logger.error("[AUTH_SERVICE] Error loading user details for email: {}", email, e);
            throw e;
        }
    }
}
