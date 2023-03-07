package com.gakdevelopers.studotest.models;

public class TestsModel {
    private String testId;
    private int topScore;
    private int time;
    private int attempt;
    private boolean live;

    public TestsModel(String testId, int topScore, int time, int attempt, boolean live) {
        this.testId = testId;
        this.topScore = topScore;
        this.time = time;
        this.attempt = attempt;
        this.live = live;
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

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
}
