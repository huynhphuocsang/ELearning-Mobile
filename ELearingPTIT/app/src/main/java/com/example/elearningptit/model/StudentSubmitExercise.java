package com.example.elearningptit.model;

import java.util.List;

public class StudentSubmitExercise {
    private int excerciseId;
    private int userId;
    private String studentCode;
    private String fullname;
    private String submitContent;
    private String documentURL;
    private String submitTime;
    private float mark;
    private int avatarId;

    public StudentSubmitExercise(int excerciseId, int userId, String studentCode, String fullname, String submitContent, String documentURL, String submitTime, float mark, int avatarId) {
        this.excerciseId = excerciseId;
        this.userId = userId;
        this.studentCode = studentCode;
        this.fullname = fullname;
        this.submitContent = submitContent;
        this.documentURL = documentURL;
        this.submitTime = submitTime;
        this.mark = mark;
        this.avatarId = avatarId;
    }

    public int getExcerciseId() {
        return excerciseId;
    }

    public void setExcerciseId(int excerciseId) {
        this.excerciseId = excerciseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSubmitContent() {
        return submitContent;
    }

    public void setSubmitContent(String submitContent) {
        this.submitContent = submitContent;
    }

    public String getDocumentURL() {
        return documentURL;
    }

    public void setDocumentURL(String documentURL) {
        this.documentURL = documentURL;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }
}
