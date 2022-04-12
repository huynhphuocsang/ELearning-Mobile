package com.example.elearningptit.model;

import java.sql.Timestamp;

public class NotificationDTO {
    private Long notificationId;
    private String notificationContent;
    private Timestamp time;
    private boolean status;

    public NotificationDTO() {
    }

    public NotificationDTO(Long notificationId, String notificationContent, Timestamp time, boolean status) {
        this.notificationId = notificationId;
        this.notificationContent = notificationContent;
        this.time = time;
        this.status = status;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}