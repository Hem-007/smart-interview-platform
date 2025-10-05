package com.smartprep.smart_interview_platform.Model;

public class LeaderBoard {

    private Long id;
    private String userName;
    private Long questionsSolved;
    private Double accuracy;
    private Long acceptedSubmissions;
    private Long totalSubmissions;

    public LeaderBoard(Long id, String userName, Long questionsSolved, Long acceptedSubmissions, Long totalSubmissions, Double accuracy) {
        this.id = id;
        this.userName = userName;
        this.questionsSolved = questionsSolved;
        this.acceptedSubmissions = acceptedSubmissions;
        this.totalSubmissions = totalSubmissions;
        this.accuracy = accuracy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getQuestionsSolved() {
        return questionsSolved;
    }

    public void setQuestionsSolved(Long questionsSolved) {
        this.questionsSolved = questionsSolved;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Long getAcceptedSubmissions() {
        return acceptedSubmissions;
    }

    public void setAcceptedSubmissions(Long acceptedSubmissions) {
        this.acceptedSubmissions = acceptedSubmissions;
    }

    public Long getTotalSubmissions() {
        return totalSubmissions;
    }

    public void setTotalSubmissions(Long totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }
}
