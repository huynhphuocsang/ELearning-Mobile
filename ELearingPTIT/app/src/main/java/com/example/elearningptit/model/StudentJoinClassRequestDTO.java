package com.example.elearningptit.model;

public class StudentJoinClassRequestDTO {
    private Long creditClassId;
    private String joinCode;

    public StudentJoinClassRequestDTO() {
    }

    public StudentJoinClassRequestDTO(Long creditClassId, String joinCode) {
        this.creditClassId = creditClassId;
        this.joinCode = joinCode;
    }

    public Long getCreditClassId() {
        return creditClassId;
    }

    public void setCreditClassId(Long creditClassId) {
        this.creditClassId = creditClassId;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }
}
