package com.example.elearningptit.model;

import java.util.List;

public class ExerciseSubmit {
    private int excerciseId;
    private String excerciseTitle;
    private String excerciseContent;
    private String startTime;
    private String endTime;
    private String submitTime;
    private String submitContent;
    private List<Document> documents;
    private Document submitFile;

    public ExerciseSubmit() {
    }

    public ExerciseSubmit(int excerciseId, String excerciseTitle, String excerciseContent, String startTime, String endTime, String submitTime, String submitContent, List<Document> documents, Document submitFile) {
        this.excerciseId = excerciseId;
        this.excerciseTitle = excerciseTitle;
        this.excerciseContent = excerciseContent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.submitTime = submitTime;
        this.submitContent = submitContent;
        this.documents = documents;
        this.submitFile = submitFile;
    }

    public int getExcerciseId() {
        return excerciseId;
    }

    public void setExcerciseId(int excerciseId) {
        this.excerciseId = excerciseId;
    }

    public String getExcerciseTitle() {
        return excerciseTitle;
    }

    public void setExcerciseTitle(String excerciseTitle) {
        this.excerciseTitle = excerciseTitle;
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

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getSubmitContent() {
        return submitContent;
    }

    public void setSubmitContent(String submitContent) {
        this.submitContent = submitContent;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Document getSubmitFile() {
        return submitFile;
    }

    public void setSubmitFile(Document submitFile) {
        this.submitFile = submitFile;
    }
}
