package com.example.elearningptit.model;

import java.sql.Timestamp;

public class PostCommentDTO {
    private Long userId;
    private String userName;
    private String avatar;
    private long commentId;
    private String content;
    private Timestamp createdAt;
    private boolean isTeacher;
    private String code;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PostCommentDTO(Long userId, String userName, String avatar, long commentId, String content, Timestamp createdAt, boolean isTeacher, String code) {
        this.userId = userId;
        this.userName = userName;
        this.avatar = avatar;
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.isTeacher = isTeacher;
        this.code = code;
    }

    public PostCommentDTO() {
    }
}
