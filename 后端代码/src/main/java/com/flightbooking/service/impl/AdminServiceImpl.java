package com.flightbooking.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flightbooking.dto.request.AdminLoginRequest;
import com.flightbooking.dto.request.AdminRegisterRequest;
import com.flightbooking.dto.response.AdminResponse;
import com.flightbooking.entity.Admin;
import com.flightbooking.repository.AdminRepository;
import com.flightbooking.service.AdminService;

import jakarta.servlet.http.HttpSession;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;

    public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder, HttpSession session) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.session = session;
    }

    @Override
    public AdminResponse register(AdminRegisterRequest request) {
        // 检查用户名是否已存在
        if (adminRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 创建管理员实体
        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setPhone(request.getPhone());
        admin.setRealName(request.getRealName());
        admin.setRole("ADMIN");
        admin.setStatus(1);

        // 保存到数据库
        Admin savedAdmin = adminRepository.save(admin);

        return AdminResponse.fromEntity(savedAdmin);
    }

    @Override
    public AdminResponse login(AdminLoginRequest request) {
        // 查找管理员
        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        // 检查状态
        if (admin.getStatus() != 1) {
            throw new RuntimeException("账户已被禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 保存到session
        session.setAttribute("admin", admin);

        return AdminResponse.fromEntity(admin);
    }

    @Override
    public AdminResponse getCurrentAdmin() {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            throw new RuntimeException("未登录");
        }
        return AdminResponse.fromEntity(admin);
    }

    @Override
    public void logout() {
        session.removeAttribute("admin");
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}