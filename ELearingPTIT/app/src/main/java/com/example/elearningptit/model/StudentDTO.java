package com.example.elearningptit.model;

public class StudentDTO {
    private String studentCode;
    private String fullnanme;

    public StudentDTO() {
    }

    public StudentDTO(String studentCode, String fullnanme) {
        this.studentCode = studentCode;
        this.fullnanme = fullnanme;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getFullnanme() {
        return fullnanme;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public void setFullnanme(String fullnanme) {
        this.fullnanme = fullnanme;
    }
}
