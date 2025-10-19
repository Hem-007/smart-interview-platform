package com.smartprep.smart_interview_platform.Security;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final String SECRET = "bXlzdXBlcnNlY3JldGtleTEyMzQ1Njc4OTA=";
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username, String role) {
        logger.info("[JWT] Generating token for user: {} with role: {}", username, role);
        try {
            String token = Jwts.builder()
                    .setSubject(username)
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
            logger.debug("[JWT] Token generated successfully for user: {}", username);
            return token;
        } catch (Exception e) {
            logger.error("[JWT] Error generating token for user: {}", username, e);
            throw e;
        }
    }

    public String extractUsername(String token) {
        logger.debug("[JWT] Extracting username from token");
        try {
            String username = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody().getSubject();
            logger.debug("[JWT] Username extracted: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("[JWT] Error extracting username from token", e);
            throw e;
        }
    }

    public boolean validateToken(String token) {
        logger.debug("[JWT] Validating token");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            logger.debug("[JWT] Token validation successful");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("[JWT] Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
