package com.example.elearningptit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JwtResponse {
    @SerializedName("accessToken")
    private String token;

    public JwtResponse() {
    }

    @SerializedName("tokenType")
    private String type;

    public JwtResponse(String token, String type, Long id, String username, List<String> roles) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    private Long id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    private String username;

    private List<String> roles;
}
