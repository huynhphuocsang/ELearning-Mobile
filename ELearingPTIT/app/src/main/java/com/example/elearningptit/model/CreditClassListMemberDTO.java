package com.example.elearningptit.model;

import java.util.List;

public class CreditClassListMemberDTO {
    private List<Teacher> teacherInfos;
    private List<Student> students;

    public CreditClassListMemberDTO() {
    }

    public CreditClassListMemberDTO(List<Teacher> teacherInfos, List<Student> students) {
        this.teacherInfos = teacherInfos;
        this.students = students;
    }

    public List<Teacher> getTeacherInfos() {
        return teacherInfos;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setTeacherInfos(List<Teacher> teacherInfos) {
        this.teacherInfos = teacherInfos;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
