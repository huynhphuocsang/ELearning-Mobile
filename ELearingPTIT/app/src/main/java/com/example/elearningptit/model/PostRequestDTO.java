package com.example.elearningptit.model;

public class PostRequestDTO {
    private String postContent;

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public PostRequestDTO(String postContent) {
        this.postContent = postContent;
    }

    public PostRequestDTO() {
    }
}
