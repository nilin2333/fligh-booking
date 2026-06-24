package com.flightbooking.dto.response;

import com.flightbooking.entity.User;

public class UserResponse {

    private Integer id;
    private String username;
    private String email;
    private String phone;
    private Integer status;

    public UserResponse() {}

    public UserResponse(Integer id, String username, String email, String phone, Integer status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getStatus() {
        return status;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setStatus(user.getStatus());
        return response;
    }
}