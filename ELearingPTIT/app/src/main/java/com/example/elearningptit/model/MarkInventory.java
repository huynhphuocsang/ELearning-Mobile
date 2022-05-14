package com.example.elearningptit.model;

public class MarkInventory {
    private float high;
    private float medium;
    private float low;
    private float veryLow;

    public MarkInventory(float high, float medium, float low, float veryLow) {
        this.high = high;
        this.medium = medium;
        this.low = low;
        this.veryLow = veryLow;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getMedium() {
        return medium;
    }

    public void setMedium(float medium) {
        this.medium = medium;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getVeryLow() {
        return veryLow;
    }

    public void setVeryLow(float veryLow) {
        this.veryLow = veryLow;
    }
}
