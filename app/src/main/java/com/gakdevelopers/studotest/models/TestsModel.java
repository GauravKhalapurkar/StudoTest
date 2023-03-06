package com.gakdevelopers.studotest.models;

public class TestsModel {
    private String testId;
    private int topScore;
    private int time;
    private int attempt;

    public TestsModel(String testId, int topScore, int time, int attempt) {
        this.testId = testId;
        this.topScore = topScore;
        this.time = time;
        this.attempt = attempt;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }
}
