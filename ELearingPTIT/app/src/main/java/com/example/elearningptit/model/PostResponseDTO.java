package com.example.elearningptit.model;

import java.sql.Timestamp;

public class PostResponseDTO {
    private Long postId;
    private String postContent;
    private Timestamp createdAt;
    private int status;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PostResponseDTO(Long postId, String postContent, Timestamp createdAt, int status) {
        this.postId = postId;
        this.postContent = postContent;
        this.createdAt = createdAt;
        this.status = status;
    }

    public PostResponseDTO() {
    }
}
