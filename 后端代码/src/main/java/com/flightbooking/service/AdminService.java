package com.flightbooking.service;

import com.flightbooking.dto.request.AdminLoginRequest;
import com.flightbooking.dto.request.AdminRegisterRequest;
import com.flightbooking.dto.response.AdminResponse;

public interface AdminService {

    AdminResponse register(AdminRegisterRequest request);

    AdminResponse login(AdminLoginRequest request);

    AdminResponse getCurrentAdmin();

    void logout();
    
    String encodePassword(String rawPassword);
}