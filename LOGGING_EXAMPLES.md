# SLF4J Logging - Example Log Outputs

This document shows example log outputs for common scenarios in the Smart Interview Platform.

## 1. User Registration Flow

```
2025-10-16 21:35:12 [INFO] [http-nio-8181-exec-1] c.s.s.C.AuthController - [AUTH] Registration attempt for email: john@example.com
2025-10-16 21:35:12 [DEBUG] [http-nio-8181-exec-1] c.s.s.S.UserService - [USER_SERVICE] Registering user with email: john@example.com
2025-10-16 21:35:12 [DEBUG] [http-nio-8181-exec-1] c.s.s.S.UserService - [USER_SERVICE] User registered successfully with ID: 42
2025-10-16 21:35:12 [INFO] [http-nio-8181-exec-1] c.s.s.C.AuthController - [AUTH] Registration successful for email: john@example.com
```

## 2. User Login Flow (Successful)

```
2025-10-16 21:36:45 [INFO] [http-nio-8181-exec-2] c.s.s.C.AuthController - [AUTH] Login attempt for email: john@example.com
2025-10-16 21:36:45 [INFO] [http-nio-8181-exec-2] c.s.s.S.UserService - [USER_SERVICE] Login attempt for email: john@example.com
2025-10-16 21:36:45 [DEBUG] [http-nio-8181-exec-2] c.s.s.S.UserService - [USER_SERVICE] User found with email: john@example.com
2025-10-16 21:36:45 [INFO] [http-nio-8181-exec-2] c.s.s.S.UserService - [USER_SERVICE] Login successful for email: john@example.com
2025-10-16 21:36:45 [INFO] [http-nio-8181-exec-2] c.s.s.Security.JwtUtil - [JWT] Generating token for user: john@example.com with role: ROLE_USER
2025-10-16 21:36:45 [DEBUG] [http-nio-8181-exec-2] c.s.s.Security.JwtUtil - [JWT] Token generated successfully for user: john@example.com
2025-10-16 21:36:45 [INFO] [http-nio-8181-exec-2] c.s.s.C.AuthController - [AUTH] Login successful for email: john@example.com with role: ROLE_USER
```

## 3. User Login Flow (Failed - Invalid Credentials)

```
2025-10-16 21:37:20 [INFO] [http-nio-8181-exec-3] c.s.s.C.AuthController - [AUTH] Login attempt for email: john@example.com
2025-10-16 21:37:20 [INFO] [http-nio-8181-exec-3] c.s.s.S.UserService - [USER_SERVICE] Login attempt for email: john@example.com
2025-10-16 21:37:20 [DEBUG] [http-nio-8181-exec-3] c.s.s.S.UserService - [USER_SERVICE] User found with email: john@example.com
2025-10-16 21:37:20 [WARN] [http-nio-8181-exec-3] c.s.s.S.UserService - [USER_SERVICE] Invalid password for email: john@example.com
2025-10-16 21:37:20 [WARN] [http-nio-8181-exec-3] c.s.s.C.AuthController - [AUTH] Login failed for email: john@example.com
2025-10-16 21:37:20 [WARN] [http-nio-8181-exec-3] c.s.s.E.GlobalExceptionHandler - [EXCEPTION] User authentication failed: Invalid credentials or user not found
```

## 4. JWT Authentication Filter Processing

```
2025-10-16 21:38:00 [DEBUG] [http-nio-8181-exec-4] c.s.s.Security.JwtAuthenticationFilter - [JWT_FILTER] Processing request: GET /api/users/profile
2025-10-16 21:38:00 [DEBUG] [http-nio-8181-exec-4] c.s.s.Security.JwtAuthenticationFilter - [JWT_FILTER] Authorization header found, extracting token
2025-10-16 21:38:00 [DEBUG] [http-nio-8181-exec-4] c.s.s.Security.JwtUtil - [JWT] Extracting username from token
2025-10-16 21:38:00 [DEBUG] [http-nio-8181-exec-4] c.s.s.Security.JwtUtil - [JWT] Username extracted: john@example.com
2025-10-16 21:38:00 [DEBUG] [http-nio-8181-exec-4] c.s.s.Security.JwtAuthenticationFilter - [JWT_FILTER] Loading user details for username: john@example.com
2025-10-16 21:38:00 [INFO] [http-nio-8181-exec-4] c.s.s.S.CustomUserDetailsService - [AUTH_SERVICE] Loading user details for email: john@example.com
2025-10-16 21:38:00 [DEBUG] [http-nio-8181-exec-4] c.s.s.S.CustomUserDetailsService - [AUTH_SERVICE] User details loaded successfully for email: john@example.com
2025-10-16 21:38:00 [DEBUG] [http-nio-8181-exec-4] c.s.s.Security.JwtUtil - [JWT] Validating token
2025-10-16 21:38:00 [DEBUG] [http-nio-8181-exec-4] c.s.s.Security.JwtUtil - [JWT] Token validation successful
2025-10-16 21:38:00 [INFO] [http-nio-8181-exec-4] c.s.s.Security.JwtAuthenticationFilter - [JWT_FILTER] Token validated successfully for user: john@example.com
2025-10-16 21:38:00 [DEBUG] [http-nio-8181-exec-4] c.s.s.Security.JwtAuthenticationFilter - [JWT_FILTER] Authentication set for user: john@example.com
```

## 5. Code Submission Flow

```
2025-10-16 21:39:15 [INFO] [http-nio-8181-exec-5] c.s.s.C.CodeSubmissionController - [SUBMISSION] Code submission attempt for user: 42, question: 15
2025-10-16 21:39:15 [DEBUG] [http-nio-8181-exec-5] c.s.s.S.CodeSubmissionService - [CODE_SUBMISSION] Validating code submission for user: 42, question: 15
2025-10-16 21:39:15 [DEBUG] [http-nio-8181-exec-5] c.s.s.S.CodeSubmissionService - [CODE_SUBMISSION] Code validation passed
2025-10-16 21:39:15 [INFO] [http-nio-8181-exec-5] c.s.s.S.CodeSubmissionService - [CODE_SUBMISSION] First solve detected for user: 42, question: 15
2025-10-16 21:39:15 [DEBUG] [http-nio-8181-exec-5] c.s.s.S.CodeSubmissionService - [CODE_SUBMISSION] Updating user stats for first solve
2025-10-16 21:39:15 [DEBUG] [http-nio-8181-exec-5] c.s.s.S.CodeSubmissionService - [CODE_SUBMISSION] Submission saved with ID: 128
2025-10-16 21:39:15 [INFO] [http-nio-8181-exec-5] c.s.s.C.CodeSubmissionController - [SUBMISSION] Code submission successful for user: 42, question: 15
```

## 6. Question Search Flow

```
2025-10-16 21:40:30 [INFO] [http-nio-8181-exec-6] c.s.s.C.QuestionController - [QUESTION] Search request with keyword: array
2025-10-16 21:40:30 [DEBUG] [http-nio-8181-exec-6] c.s.s.S.QuestionService - [QUESTION_SERVICE] Searching questions with keyword: array
2025-10-16 21:40:30 [DEBUG] [http-nio-8181-exec-6] c.s.s.S.QuestionService - [QUESTION_SERVICE] Found 5 questions matching keyword: array
2025-10-16 21:40:30 [INFO] [http-nio-8181-exec-6] c.s.s.C.QuestionController - [QUESTION] Search completed, returned 5 results
```

## 7. Leaderboard Generation

```
2025-10-16 21:41:45 [INFO] [http-nio-8181-exec-7] c.s.s.C.UserStatsController - [LEADERBOARD] Fetching leaderboard of type: questionsSolved
2025-10-16 21:41:45 [INFO] [http-nio-8181-exec-7] c.s.s.S.UserStatsService - [STATS_SERVICE] Fetching leaderboard of type: questionsSolved
2025-10-16 21:41:45 [DEBUG] [http-nio-8181-exec-7] c.s.s.S.UserStatsService - [STATS_SERVICE] Sorting leaderboard by questions solved
2025-10-16 21:41:45 [INFO] [http-nio-8181-exec-7] c.s.s.S.UserStatsService - [STATS_SERVICE] Leaderboard generated with 25 entries for type: questionsSolved
2025-10-16 21:41:45 [INFO] [http-nio-8181-exec-7] c.s.s.C.UserStatsController - [LEADERBOARD] Leaderboard returned with 25 entries
```

## 8. Exception Handling - Resource Not Found

```
2025-10-16 21:42:10 [INFO] [http-nio-8181-exec-8] c.s.s.C.QuestionController - [QUESTION] Fetching question with ID: 999
2025-10-16 21:42:10 [DEBUG] [http-nio-8181-exec-8] c.s.s.S.QuestionService - [QUESTION_SERVICE] Retrieving question with ID: 999
2025-10-16 21:42:10 [WARN] [http-nio-8181-exec-8] c.s.s.E.GlobalExceptionHandler - [EXCEPTION] Resource not found: Question not found with ID: 999
```

## 9. Exception Handling - Invalid Token

```
2025-10-16 21:43:25 [DEBUG] [http-nio-8181-exec-9] c.s.s.Security.JwtAuthenticationFilter - [JWT_FILTER] Processing request: GET /api/users/profile
2025-10-16 21:43:25 [DEBUG] [http-nio-8181-exec-9] c.s.s.Security.JwtAuthenticationFilter - [JWT_FILTER] Authorization header found, extracting token
2025-10-16 21:43:25 [DEBUG] [http-nio-8181-exec-9] c.s.s.Security.JwtUtil - [JWT] Validating token
2025-10-16 21:43:25 [WARN] [http-nio-8181-exec-9] c.s.s.Security.JwtUtil - [JWT] Token validation failed: JWT signature does not match locally computed signature
2025-10-16 21:43:25 [WARN] [http-nio-8181-exec-9] c.s.s.Security.JwtAuthenticationFilter - [JWT_FILTER] Token validation failed for user: null
```

## 10. Health Check

```
2025-10-16 21:44:00 [DEBUG] [http-nio-8181-exec-10] c.s.s.C.HomeController - [HOME] Health check endpoint accessed
2025-10-16 21:44:00 [INFO] [http-nio-8181-exec-10] c.s.s.C.HomeController - [HOME] Application is running
```

## Log File Location

All logs are written to: `logs/smart-interview-platform.log`

Example log file content:
```
2025-10-16 21:35:12 [INFO] [http-nio-8181-exec-1] c.s.s.C.AuthController - [AUTH] Registration attempt for email: john@example.com
2025-10-16 21:35:12 [DEBUG] [http-nio-8181-exec-1] c.s.s.S.UserService - [USER_SERVICE] Registering user with email: john@example.com
2025-10-16 21:35:12 [DEBUG] [http-nio-8181-exec-1] c.s.s.S.UserService - [USER_SERVICE] User registered successfully with ID: 42
2025-10-16 21:35:12 [INFO] [http-nio-8181-exec-1] c.s.s.C.AuthController - [AUTH] Registration successful for email: john@example.com
...
```

## Filtering Logs

### By Module
```bash
grep "\[AUTH\]" logs/smart-interview-platform.log
grep "\[SUBMISSION\]" logs/smart-interview-platform.log
grep "\[QUESTION\]" logs/smart-interview-platform.log
```

### By Level
```bash
grep "\[ERROR\]" logs/smart-interview-platform.log
grep "\[WARN\]" logs/smart-interview-platform.log
grep "\[INFO\]" logs/smart-interview-platform.log
```

### By User
```bash
grep "john@example.com" logs/smart-interview-platform.log
```

### By Time Range
```bash
grep "2025-10-16 21:3[5-9]" logs/smart-interview-platform.log
```

