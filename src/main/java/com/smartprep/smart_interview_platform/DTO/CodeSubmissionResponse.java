package com.smartprep.smart_interview_platform.DTO;

import java.time.LocalDateTime;

/**
 * CodeSubmissionResponse DTO
 * 
 * Data Transfer Object for returning code submission details including AI-generated feedback.
 * Used to provide a structured response to clients with all relevant submission information.
 * 
 * @author Smart Interview Platform
 * @version 1.0
 */
public class CodeSubmissionResponse {

    private Long id;
    private String code;
    private String status;
    private String language;
    private LocalDateTime submittedAt;
    private String feedback;
    private Long userId;
    private String userName;
    private Long questionId;
    private String questionTitle;
    private String message;

    // Constructors
    public CodeSubmissionResponse() {
    }

    public CodeSubmissionResponse(Long id, String code, String status, String language,
                                  LocalDateTime submittedAt, String feedback,
                                  Long userId, String userName, Long questionId, String questionTitle) {
        this.id = id;
        this.code = code;
        this.status = status;
        this.language = language;
        this.submittedAt = submittedAt;
        this.feedback = feedback;
        this.userId = userId;
        this.userName = userName;
        this.questionId = questionId;
        this.questionTitle = questionTitle;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

