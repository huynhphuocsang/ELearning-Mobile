package com.example.elearningptit.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FolderDTOResponse {
    private Long folderId;
    private String folderName;
    private Date upTime;
    private Long parentsFolder;
    private Set<DocumentDTO> documents = new HashSet<>();

    public FolderDTOResponse() {
    }

    public FolderDTOResponse(Long folderId, String folderName, Date upTime, Long parentsFolder, Set<DocumentDTO> documents) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.upTime = upTime;
        this.parentsFolder = parentsFolder;
        this.documents = documents;
    }

    public Long getFolderId() {
        return folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public Date getUpTime() {
        return upTime;
    }

    public Long getParentsFolder() {
        return parentsFolder;
    }

    public Set<DocumentDTO> getDocuments() {
        return documents;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    public void setParentsFolder(Long parentsFolder) {
        this.parentsFolder = parentsFolder;
    }

    public void setDocuments(Set<DocumentDTO> documents) {
        this.documents = documents;
    }
}
