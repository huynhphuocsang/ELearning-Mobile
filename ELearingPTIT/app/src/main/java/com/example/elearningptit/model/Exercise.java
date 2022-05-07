package com.example.elearningptit.model;

public class Exercise {
    private int excerciseId;
    private String title;
    private String excerciseContent;
    private String startTime;
    private String endTime;

    public Exercise(int excerciseId, String title, String excerciseContent, String startTime, String endTime) {
        this.excerciseId = excerciseId;
        this.title = title;
        this.excerciseContent = excerciseContent;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getExcerciseId() {
        return excerciseId;
    }

    public void setExcerciseId(int excerciseId) {
        this.excerciseId = excerciseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerciseContent() {
        return excerciseContent;
    }

    public void setExcerciseContent(String excerciseContent) {
        this.excerciseContent = excerciseContent;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
