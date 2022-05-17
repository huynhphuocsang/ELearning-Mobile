package com.example.elearningptit.model;

public class FolderRequest {
    private Long creditClassId;
    private String folderName;
    private Long parentId;

    public FolderRequest() {
    }

    public FolderRequest(Long creditClassId, String folderName, Long parentId) {
        this.creditClassId = creditClassId;
        this.folderName = folderName;
        this.parentId = parentId;
    }

    public Long getCreditClassId() {
        return creditClassId;
    }

    public String getFolderName() {
        return folderName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setCreditClassId(Long creditClassId) {
        this.creditClassId = creditClassId;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
