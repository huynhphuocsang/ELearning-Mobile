package com.example.elearningptit.model;

public class UpdatePasswordRequestWithVerify {
    private String key;
    private String password;

    public UpdatePasswordRequestWithVerify() {
    }

    public UpdatePasswordRequestWithVerify(String key, String password) {
        this.key = key;
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
