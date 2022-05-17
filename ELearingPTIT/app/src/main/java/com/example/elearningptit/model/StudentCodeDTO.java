package com.example.elearningptit.model;

import java.util.List;

public class StudentCodeDTO {
    private List<String> studentCode;

    public StudentCodeDTO() {
    }

    public StudentCodeDTO(List<String> studentCode) {
        this.studentCode = studentCode;
    }

    public List<String> getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(List<String> studentCode) {
        this.studentCode = studentCode;
    }
}
