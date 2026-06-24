package com.flightbooking.dto.response;

import com.flightbooking.entity.Admin;

public class AdminResponse {

    private Integer id;
    private String username;
    private String email;
    private String phone;
    private String realName;
    private String role;
    private Integer status;

    public AdminResponse() {}

    public AdminResponse(Integer id, String username, String email, String phone, String realName, String role, Integer status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.realName = realName;
        this.role = role;
        this.status = status;
    }

    public static AdminResponse fromEntity(Admin admin) {
        return new AdminResponse(
            admin.getId(),
            admin.getUsername(),
            admin.getEmail(),
            admin.getPhone(),
            admin.getRealName(),
            admin.getRole(),
            admin.getStatus()
        );
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}