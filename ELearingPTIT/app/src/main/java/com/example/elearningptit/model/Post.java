package com.example.elearningptit.model;

public class Post {
    private int postId;
    private String avartarPublisher;
    private String fullname;
    private String creditClassId;
    private String subjectName;
    private String postedTime;
    private String postContent;

    public Post(int postId, String avartarPublisher, String fullname, String creditClassId, String subjectName, String postedTime, String postContent) {
        this.postId = postId;
        this.avartarPublisher = avartarPublisher;
        this.fullname = fullname;
        this.creditClassId = creditClassId;
        this.subjectName = subjectName;
        this.postedTime = postedTime;
        this.postContent = postContent;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getAvartarPublisher() {
        return avartarPublisher;
    }

    public void setAvartarPublisher(String avartarPublisher) {
        this.avartarPublisher = avartarPublisher;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCreditClassId() {
        return creditClassId;
    }

    public void setCreditClassId(String creditClassId) {
        this.creditClassId = creditClassId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(String postedTime) {
        this.postedTime = postedTime;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
}
