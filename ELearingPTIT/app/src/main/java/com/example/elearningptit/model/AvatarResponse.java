package com.example.elearningptit.model;

public class AvatarResponse {
    private String fileName;
    private String dowloadURL;
    private String fileType;
    private long fileSize;

    public AvatarResponse() {
    }

    public AvatarResponse(String fileName, String dowloadURL, String fileType, long fileSize) {
        this.fileName = fileName;
        this.dowloadURL = dowloadURL;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDowloadURL() {
        return dowloadURL;
    }

    public void setDowloadURL(String dowloadURL) {
        this.dowloadURL = dowloadURL;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
