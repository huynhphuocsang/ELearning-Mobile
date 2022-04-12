package com.example.elearningptit.model;

import java.util.List;

public class NotificationPageForUser {
    private int totalPage;
    List<NotificationDTO> notifications;

    public NotificationPageForUser(int totalPage, List<NotificationDTO> notifications) {
        this.totalPage = totalPage;
        this.notifications = notifications;
    }

    public NotificationPageForUser() {
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }
}
