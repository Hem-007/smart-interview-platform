# SLF4J Logging - Quick Reference Guide

## How to Use Logging in Your Code

### 1. Import SLF4J
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
```

### 2. Initialize Logger in Class
```java
private static final Logger logger = LoggerFactory.getLogger(YourClassName.class);
```

### 3. Log Messages

#### INFO Level (Normal flow, key operations)
```java
logger.info("[MODULE] Operation successful for user: {}", userId);
```

#### DEBUG Level (Detailed information)
```java
logger.debug("[MODULE] Processing request with parameters: {}", params);
```

#### WARN Level (Non-critical issues)
```java
logger.warn("[MODULE] User not found with email: {}", email);
```

#### ERROR Level (Exceptions and failures)
```java
logger.error("[MODULE] Error processing request", exception);
// or with message
logger.error("[MODULE] Failed to save user: {}", userId, exception);
```

## Module Prefixes Used

| Module | Prefix | Location |
|--------|--------|----------|
| Authentication | [AUTH] | AuthController |
| User Management | [USER] | UserController, UserService |
| Code Submission | [SUBMISSION] | CodeSubmissionController, CodeSubmissionService |
| Questions | [QUESTION] | QuestionController, QuestionService |
| Leaderboard | [LEADERBOARD] | UserStatsController |
| Stats Service | [STATS_SERVICE] | UserStatsService |
| Auth Service | [AUTH_SERVICE] | CustomUserDetailsService |
| JWT | [JWT] | JwtUtil |
| JWT Filter | [JWT_FILTER] | JwtAuthenticationFilter |
| Home | [HOME] | HomeController |
| Exception | [EXCEPTION] | GlobalExceptionHandler |

## Log Output Locations

### Console Output
- Real-time logs in IDE/terminal
- Format: `TIMESTAMP [LEVEL] [THREAD] LOGGER - MESSAGE`

### File Output
- Location: `logs/smart-interview-platform.log`
- Max size: 10MB per file
- Retention: 10 days of history
- Auto-rotation enabled

## Configuration File

**Location:** `src/main/resources/application.properties`

```properties
# Root level (production)
logging.level.root=INFO

# Application level (detailed)
logging.level.com.smartprep=DEBUG

# Security level (authentication tracking)
logging.level.org.springframework.security=DEBUG

# File configuration
logging.file.name=logs/smart-interview-platform.log
logging.file.max-size=10MB
logging.file.max-history=10

# Output format
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%-5level] [%thread] %logger{36} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%-5level] [%thread] %logger{36} - %msg%n
```

## Best Practices

### ✅ DO:
- Use parameterized logging: `logger.info("User: {}", email)`
- Log at appropriate levels (INFO for normal, DEBUG for details)
- Include context: user ID, request ID, operation type
- Log before and after critical operations
- Log exceptions with full stack trace
- Use consistent module prefixes

### ❌ DON'T:
- Log sensitive data: passwords, tokens, API keys
- Use string concatenation: `logger.info("User: " + email)` (performance issue)
- Log at DEBUG level for production (too verbose)
- Ignore exceptions without logging
- Use System.out.println() (use logger instead)
- Log the same information multiple times

## Common Logging Patterns

### Successful Operation
```java
logger.info("[MODULE] Operation completed successfully for user: {}", userId);
```

### Failed Operation
```java
logger.error("[MODULE] Operation failed for user: {}", userId, exception);
```

### Data Retrieval
```java
logger.debug("[MODULE] Retrieved {} records from database", count);
```

### Validation Failure
```java
logger.warn("[MODULE] Validation failed: {}", reason);
```

### Authentication
```java
logger.info("[AUTH] Login attempt for email: {}", email);
logger.info("[AUTH] Login successful for email: {}", email);
logger.warn("[AUTH] Login failed for email: {}", email);
```

### Exception Handling
```java
try {
    // operation
    logger.info("[MODULE] Operation completed");
} catch (Exception e) {
    logger.error("[MODULE] Error during operation", e);
    throw e;
}
```

## Viewing Logs

### Real-time Console
- Watch IDE console output while running application
- Filter by log level or module prefix

### Log File
```bash
# View entire log file
cat logs/smart-interview-platform.log

# View last 100 lines
tail -100 logs/smart-interview-platform.log

# Search for specific module
grep "\[AUTH\]" logs/smart-interview-platform.log

# Search for errors
grep "\[ERROR\]" logs/smart-interview-platform.log
```

## Troubleshooting

### Logs not appearing?
1. Check `application.properties` logging configuration
2. Verify logger is initialized correctly
3. Check log level (DEBUG messages won't show if level is INFO)
4. Ensure logs directory exists and is writable

### Too many logs?
1. Increase log level in `application.properties`
2. Change `logging.level.com.smartprep=INFO` (from DEBUG)
3. Reduce DEBUG statements in code

### Log file too large?
1. Logs auto-rotate at 10MB
2. Check `logging.file.max-history` setting
3. Implement log cleanup policy

## Integration with GlobalExceptionHandler

All unhandled exceptions are automatically logged by `GlobalExceptionHandler`:
- ResourceNotFoundException → 404 (WARN level)
- UsernameNotFoundException → 401 (WARN level)
- BadCredentialsException → 401 (WARN level)
- RuntimeException → 400 (ERROR level)
- Generic Exception → 500 (ERROR level)

No additional logging needed in exception handlers!

## Performance Considerations

- Parameterized logging is lazy-evaluated (efficient)
- DEBUG logs only evaluated if DEBUG level is enabled
- File I/O is asynchronous (non-blocking)
- Log rotation prevents unbounded file growth

