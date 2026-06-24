package com.flightbooking.service;

import com.flightbooking.dto.request.LoginRequest;
import com.flightbooking.dto.request.RegisterRequest;
import com.flightbooking.dto.response.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);

    UserResponse login(LoginRequest request);

    UserResponse getCurrentUser();

    void logout();
}