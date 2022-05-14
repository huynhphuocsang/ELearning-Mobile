package com.example.elearningptit.model;

public class MarkDTO {
    private int excerciseId;
    private float mark;
    private long userId;

    public MarkDTO(int excerciseId, float mark, long userId) {
        this.excerciseId = excerciseId;
        this.mark = mark;
        this.userId = userId;
    }

    public int getExcerciseId() {
        return excerciseId;
    }

    public void setExcerciseId(int excerciseId) {
        this.excerciseId = excerciseId;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
