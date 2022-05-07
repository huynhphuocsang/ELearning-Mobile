package com.example.elearningptit.model;

import java.util.List;

public class Folder {

    private int folderId;
    private String folderName;
    private String upTime;
    private int parentsFolder;
    private List<Document> documents;


    public Folder() {
    }

    public Folder(int folderId, String folderName, String upTime, int parentsFolder, List<Document> documents) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.upTime = upTime;
        this.parentsFolder = parentsFolder;
        this.documents = documents;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public int getParentsFolder() {
        return parentsFolder;
    }

    public void setParentsFolder(int parentsFolder) {
        this.parentsFolder = parentsFolder;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
