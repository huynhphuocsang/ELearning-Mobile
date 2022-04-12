package com.example.elearningptit.model;

import java.util.List;

public class UserInfo {
    private  Long userId;
    private String avatar;
    private String fullname;
    private List<String> roles;
    private String email;
    private String phone;
    private String address;
    private boolean gender;
    private String dateOfBirth;
    private String userCode;
    private String userClass;


    public UserInfo() {
    }

    public UserInfo(Long userId, String avatar, String fullname, List<String> roles, String email, String phone, String address, boolean gender, String dateOfBirth, String userCode, String userClass) {
        this.userId = userId;
        this.avatar = avatar;
        this.fullname = fullname;
        this.roles = roles;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.userCode = userCode;
        this.userClass = userClass;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }
}
