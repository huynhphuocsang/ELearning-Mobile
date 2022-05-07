package com.example.elearningptit.model;

import java.util.List;

public class CreditClassDetail {
    private String creditClassName;
    private List<Teacher> teacherInfos;
    private String departmentName;
    private String startTime;
    private String endTime;
    private List<Exercise> excercises;
    private List<Folder> folders;
    private List<Post> listPost;

    public CreditClassDetail(String creditClassName, List<Teacher> teacherInfos, String departmentName, String startTime, String endTime, List<Exercise> excercises, List<Folder> folders, List<Post> listPost) {
        this.creditClassName = creditClassName;
        this.teacherInfos = teacherInfos;
        this.departmentName = departmentName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.excercises = excercises;
        this.folders = folders;
        this.listPost = listPost;
    }

    public String getCreditClassName() {
        return creditClassName;
    }

    public void setCreditClassName(String creditClassName) {
        this.creditClassName = creditClassName;
    }

    public List<Teacher> getTeacherInfos() {
        return teacherInfos;
    }

    public void setTeacherInfos(List<Teacher> teacherInfos) {
        this.teacherInfos = teacherInfos;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public List<Exercise> getExcercises() {
        return excercises;
    }

    public void setExcercises(List<Exercise> excercises) {
        this.excercises = excercises;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<Post> getListPost() {
        return listPost;
    }

    public void setListPost(List<Post> listPost) {
        this.listPost = listPost;
    }
}
