package com.example.elearningptit.model;

import java.util.List;

public class ExerciseSubmit {
    private Exercise exercise;
    private String submitTime;
    private String submitContent;
    private List<Document> documents;
    private Document submitFile;

    public ExerciseSubmit() {
    }

    public ExerciseSubmit(Exercise exercise, String submitTime, String submitContent, List<Document> documents, Document submitFile) {
        this.exercise = exercise;
        this.submitTime = submitTime;
        this.submitContent = submitContent;
        this.documents = documents;
        this.submitFile = submitFile;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
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
