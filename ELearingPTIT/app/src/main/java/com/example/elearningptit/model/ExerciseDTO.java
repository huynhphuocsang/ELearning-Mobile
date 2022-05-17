package com.example.elearningptit.model;

import java.sql.Timestamp;

import okhttp3.MultipartBody;

public class ExerciseDTO {
    private String title;
    private String excerciseContent;

    private Timestamp startTime;
    private Timestamp endTime;
    private Long creditClassId;
    private MultipartBody.Part file[];

    public ExerciseDTO(String title, String excerciseContent, Timestamp startTime, Timestamp endTime, Long creditClassId, MultipartBody.Part[] file) {
        this.title = title;
        this.excerciseContent = excerciseContent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.creditClassId = creditClassId;
        this.file = file;
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Long getCreditClassId() {
        return creditClassId;
    }

    public void setCreditClassId(Long creditClassId) {
        this.creditClassId = creditClassId;
    }

    public MultipartBody.Part[] getFile() {
        return file;
    }

    public void setFile(MultipartBody.Part[] file) {
        this.file = file;
    }
}
