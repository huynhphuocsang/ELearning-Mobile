package com.example.elearningptit.model;

public class PostDTOWithComment {
    private long postId;
    private String avartarPublisher;
    private String creditClassId;
    private String fullname;
    private String postContent;
    private String postedTime;
    private String subjectName;
    private int quantityComments;

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getAvartarPublisher() {
        return avartarPublisher;
    }

    public void setAvartarPublisher(String avartarPublisher) {
        this.avartarPublisher = avartarPublisher;
    }

    public String getCreditClassId() {
        return creditClassId;
    }

    public void setCreditClassId(String creditClassId) {
        this.creditClassId = creditClassId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(String postedTime) {
        this.postedTime = postedTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getQuantityComments() {
        return quantityComments;
    }

    public void setQuantityComments(int quantityComments) {
        this.quantityComments = quantityComments;
    }

    public PostDTOWithComment(long postId, String avartarPublisher, String creditClassId, String fullname, String postContent, String postedTime, String subjectName, int quantityComments) {
        this.postId = postId;
        this.avartarPublisher = avartarPublisher;
        this.creditClassId = creditClassId;
        this.fullname = fullname;
        this.postContent = postContent;
        this.postedTime = postedTime;
        this.subjectName = subjectName;
        this.quantityComments = quantityComments;
    }

    public PostDTOWithComment() {
    }
}
