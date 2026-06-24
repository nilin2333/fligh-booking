package com.flightbooking.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightbooking.dto.request.LoginRequest;
import com.flightbooking.dto.request.RegisterRequest;
import com.flightbooking.dto.response.UserResponse;
import com.flightbooking.entity.User;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.service.UserService;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final HttpSession httpSession;

    public UserServiceImpl(UserRepository userRepository, 
                          BCryptPasswordEncoder passwordEncoder,
                          HttpSession httpSession) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.httpSession = httpSession;
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(1);

        User savedUser = userRepository.save(user);
        return UserResponse.fromEntity(savedUser);
    }

    @Override
    public UserResponse login(LoginRequest request) {
        // 调试日志：打印登录请求
        System.out.println("登录请求: username=" + request.getUsername());
        
        User user = userRepository.findByUsernameAndStatus(request.getUsername(), 1)
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        // 调试日志：打印查询到的用户
        System.out.println("查询到的用户: id=" + user.getId() + ", username=" + user.getUsername());
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("username", user.getUsername());
        httpSession.setMaxInactiveInterval(3600);

        return UserResponse.fromEntity(user);
    }

    @Override
    public UserResponse getCurrentUser() {
        Integer userId = (Integer) httpSession.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return UserResponse.fromEntity(user);
    }

    @Override
    public void logout() {
        httpSession.invalidate();
    }
}