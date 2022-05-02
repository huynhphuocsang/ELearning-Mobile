package com.example.elearningptit.model;

import java.util.List;

public class CreditClass {
    private Long creditClassId;
    private String subjectName;
    private List<String> teachers;
    private String departmentName;
    private String schoolYear;
    private int semester;

    public CreditClass() {
    }

    public CreditClass(Long creditClassId, String subjectName, List<String> teachers, String departmentName, String schoolYear, int semester) {
        this.creditClassId = creditClassId;
        this.subjectName = subjectName;
        this.teachers = teachers;
        this.departmentName = departmentName;
        this.schoolYear = schoolYear;
        this.semester = semester;
    }

    public Long getCreditClassId() {
        return creditClassId;
    }

    public void setCreditClassId(Long creditClassId) {
        this.creditClassId = creditClassId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}
