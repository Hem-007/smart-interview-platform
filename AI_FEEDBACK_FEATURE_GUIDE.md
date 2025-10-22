# AI-Based Code Feedback Generation Feature

## Overview

The Smart Interview Platform now includes an intelligent AI-based feedback system that analyzes code submissions and provides expert-level feedback on correctness, efficiency, and coding style. This feature integrates with OpenAI's GPT-4 API to deliver production-ready code reviews.

## Architecture

### Components

1. **FeedbackService Interface** - Defines the contract for feedback generation
2. **FeedbackServiceImpl** - OpenAI GPT-4 integration implementation
3. **CodeSubmission Entity** - Updated with `feedback` field (2000 chars)
4. **CodeSubmissionService** - Integrated feedback generation into submission workflow
5. **CodeSubmissionController** - Returns feedback in API response
6. **CodeSubmissionResponse DTO** - Structured response with feedback

### Data Flow

```
User submits code
    ↓
CodeSubmissionController receives request
    ↓
CodeSubmissionService.submitCode() called
    ↓
Code saved to database
    ↓
FeedbackService.generateFeedback() called
    ↓
OpenAI GPT-4 API processes code
    ↓
Feedback stored in CodeSubmission.feedback field
    ↓
CodeSubmissionResponse returned to client with feedback
```

## Setup & Configuration

### 1. Get OpenAI API Key

1. Visit [OpenAI Platform](https://platform.openai.com/api-keys)
2. Sign up or log in to your account
3. Create a new API key
4. Copy the key (you won't see it again!)

### 2. Configure application.properties

Add the following to `src/main/resources/application.properties`:

```properties
# OpenAI Configuration for AI Feedback Generation
openai.api.key=sk-your-actual-api-key-here
openai.model=gpt-4
openai.max-tokens=1000
openai.temperature=0.7
```

**Configuration Options:**

| Property | Default | Description |
|----------|---------|-------------|
| `openai.api.key` | (required) | Your OpenAI API key |
| `openai.model` | gpt-4 | Model to use (gpt-4, gpt-3.5-turbo) |
| `openai.max-tokens` | 1000 | Max response length |
| `openai.temperature` | 0.7 | Creativity level (0-1) |

### 3. Environment Variable (Recommended for Production)

Instead of hardcoding the API key, use environment variables:

```properties
openai.api.key=${OPENAI_API_KEY:}
```

Then set the environment variable:
```bash
export OPENAI_API_KEY=sk-your-actual-api-key-here
```

## API Usage

### Submit Code with Feedback

**Endpoint:** `POST /submission/submit`

**Request:**
```json
{
  "code": "public int add(int a, int b) { return a + b; }",
  "language": "Java",
  "status": "Accepted",
  "user": { "id": 1 },
  "question": { "id": 1 }
}
```

**Response:**
```json
{
  "id": 1,
  "code": "public int add(int a, int b) { return a + b; }",
  "language": "Java",
  "status": "Accepted",
  "submittedAt": "2025-10-16T21:45:30",
  "feedback": "Your code is correct and efficient. The logic is straightforward and easy to understand. Consider adding input validation for edge cases.",
  "userId": 1,
  "userName": "John Doe",
  "questionId": 1,
  "questionTitle": "Two Sum",
  "message": "✅ Code successfully submitted by John Doe for the question \"Two Sum\""
}
```

## Feedback Generation Details

### System Prompt

The AI is guided by this system prompt to provide structured feedback:

```
You are an expert coding interviewer and software engineer. Your task is to review code submissions
and provide constructive, structured feedback. Focus on:

1. Code Correctness: Does the code solve the problem correctly? Are there any logical errors?
2. Time & Space Complexity: Analyze the algorithm's efficiency. Suggest optimizations if needed.
3. Coding Style & Readability: Is the code clean, well-structured, and easy to understand?
4. Best Practices: Does the code follow language conventions and best practices?
5. Improvement Suggestions: Provide specific, actionable suggestions for improvement.

Format your response as clear, concise text without JSON or markdown formatting.
Be encouraging but honest. Keep feedback to 500 words maximum.
```

### Supported Languages

The system supports feedback for any programming language:
- Java
- Python
- C++
- JavaScript
- Go
- Rust
- C#
- PHP
- Ruby
- And more...

## Error Handling

### Graceful Degradation

If feedback generation fails, the submission is still saved with a fallback message:

```
"Feedback generation unavailable at this time."
```

### Common Issues

| Issue | Solution |
|-------|----------|
| API key not configured | Set `openai.api.key` in application.properties |
| Rate limit exceeded | Wait before submitting more code |
| API timeout | Increase `openai.max-tokens` or reduce code size |
| Empty feedback | Check code and language parameters |

## Logging

All feedback operations are logged with the `[FEEDBACK_SERVICE]` prefix:

```
[FEEDBACK_SERVICE] Generating AI feedback for Java code submission
[FEEDBACK_SERVICE] Initializing OpenAI service with model: gpt-4
[FEEDBACK_SERVICE] Sending request to OpenAI API
[FEEDBACK_SERVICE] AI feedback generated successfully for Java code
```

## Performance Considerations

### API Costs

OpenAI charges per token used. Typical feedback generation costs:
- **GPT-4:** ~$0.03-0.06 per submission
- **GPT-3.5-turbo:** ~$0.001-0.002 per submission

### Optimization Tips

1. **Use GPT-3.5-turbo for faster/cheaper responses:**
   ```properties
   openai.model=gpt-3.5-turbo
   ```

2. **Reduce max tokens for shorter feedback:**
   ```properties
   openai.max-tokens=500
   ```

3. **Lower temperature for more consistent feedback:**
   ```properties
   openai.temperature=0.5
   ```

## Testing

### Unit Tests

Run the comprehensive test suite:

```bash
mvn test -Dtest=FeedbackServiceTest
```

**Test Coverage:**
- ✅ Successful feedback generation
- ✅ Null/empty code validation
- ✅ Null/empty language validation
- ✅ Missing API key handling
- ✅ Multiple programming languages
- ✅ Long code submissions
- ✅ Special characters in code
- ✅ Empty API responses
- ✅ API error handling

### Manual Testing

1. Start the application
2. Submit code via `/submission/submit` endpoint
3. Check the response for `feedback` field
4. Verify logs show `[FEEDBACK_SERVICE]` entries

## Database Migration

The `feedback` field is automatically added to the `CodeSubmissions` table:

```sql
ALTER TABLE CodeSubmissions ADD COLUMN feedback VARCHAR(2000);
```

This happens automatically with `spring.jpa.hibernate.ddl-auto=update`

## Security Considerations

### API Key Protection

- ✅ Never commit API keys to version control
- ✅ Use environment variables in production
- ✅ Rotate API keys regularly
- ✅ Monitor API usage for suspicious activity

### Data Privacy

- ✅ Code submissions are only sent to OpenAI for feedback
- ✅ Feedback is stored locally in the database
- ✅ No sensitive data is logged
- ✅ Comply with OpenAI's data retention policies

## Future Enhancements

1. **Caching** - Cache feedback for identical code submissions
2. **Async Processing** - Generate feedback asynchronously
3. **Custom Prompts** - Allow users to customize feedback focus areas
4. **Multiple AI Models** - Support Claude, Gemini, etc.
5. **Feedback History** - Track feedback improvements over time
6. **Performance Metrics** - Analyze feedback impact on code quality

## Troubleshooting

### Feedback not appearing in response

1. Check if API key is configured
2. Check logs for `[FEEDBACK_SERVICE]` errors
3. Verify OpenAI API status
4. Check API usage limits

### Slow feedback generation

1. Reduce `openai.max-tokens`
2. Switch to `gpt-3.5-turbo` model
3. Check network connectivity
4. Monitor OpenAI API status

### Inconsistent feedback

1. Adjust `openai.temperature` (lower = more consistent)
2. Ensure code is properly formatted
3. Provide clear language specification

## Support & Documentation

- **OpenAI API Docs:** https://platform.openai.com/docs
- **GitHub Issues:** Report bugs and feature requests
- **Logging:** Check `logs/smart-interview-platform.log` for detailed information

## Version History

- **v1.0** (2025-10-16) - Initial release with GPT-4 integration

