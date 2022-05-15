package com.example.elearningptit.model;

import java.util.List;

public class Student {
    private String studentCode;
    private String fullnanme;

    public Student(List<Student> students) {
    }

    public Student(String studentCode, String fullnanme) {
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
