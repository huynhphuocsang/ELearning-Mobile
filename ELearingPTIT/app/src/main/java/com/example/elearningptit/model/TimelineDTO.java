package com.example.elearningptit.model;

public class TimelineDTO {
    private Long creditClass;
    private String subjectName;
    private int semester;
    private String schoolYear;
    private String startTime;
    private String endTime;
    private int dayOfWeek;
    private String room;
    private int startLesson;
    private int endLesson;

    public TimelineDTO() {
    }

    public TimelineDTO(Long creditClass, String subjectName, int semester, String schoolYear, String startTime, String endTime, int dayOfWeek, String room, int startLesson, int endLesson) {
        this.creditClass = creditClass;
        this.subjectName = subjectName;
        this.semester = semester;
        this.schoolYear = schoolYear;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.room = room;
        this.startLesson = startLesson;
        this.endLesson = endLesson;
    }

    public Long getCreditClass() {
        return creditClass;
    }

    public void setCreditClass(Long creditClass) {
        this.creditClass = creditClass;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getStartLesson() {
        return startLesson;
    }

    public void setStartLesson(int startLesson) {
        this.startLesson = startLesson;
    }

    public int getEndLesson() {
        return endLesson;
    }

    public void setEndLesson(int endLesson) {
        this.endLesson = endLesson;
    }
}
