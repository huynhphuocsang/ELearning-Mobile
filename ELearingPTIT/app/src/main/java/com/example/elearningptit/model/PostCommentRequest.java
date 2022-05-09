package com.example.elearningptit.model;

public class PostCommentRequest {
    private Long postId;
    private String content;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostCommentRequest(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }

    public PostCommentRequest() {
    }
}
