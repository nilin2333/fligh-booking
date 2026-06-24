package com.flightbooking.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightbooking.dto.request.AdminLoginRequest;
import com.flightbooking.dto.request.AdminRegisterRequest;
import com.flightbooking.dto.response.AdminResponse;
import com.flightbooking.dto.response.ApiResponse;
import com.flightbooking.entity.Admin;
import com.flightbooking.repository.AdminRepository;
import com.flightbooking.repository.FlightRepository;
import com.flightbooking.repository.OrderRepository;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;
    private final FlightRepository flightRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AdminController(AdminService adminService, 
                          FlightRepository flightRepository,
                          OrderRepository orderRepository,
                          UserRepository userRepository,
                          AdminRepository adminRepository) {
        this.adminService = adminService;
        this.flightRepository = flightRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AdminResponse>> register(@Valid @RequestBody AdminRegisterRequest request) {
        try {
            AdminResponse admin = adminService.register(request);
            return ResponseEntity.ok(ApiResponse.success("注册成功", admin));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminResponse>> login(@Valid @RequestBody AdminLoginRequest request) {
        try {
            AdminResponse admin = adminService.login(request);
            return ResponseEntity.ok(ApiResponse.success("登录成功", admin));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<AdminResponse>> getCurrentAdmin() {
        try {
            AdminResponse admin = adminService.getCurrentAdmin();
            return ResponseEntity.ok(ApiResponse.success("获取成功", admin));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        adminService.logout();
        return ResponseEntity.ok(ApiResponse.success("登出成功"));
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 航班总数
            long totalFlights = flightRepository.count();
            stats.put("totalFlights", totalFlights);
            
            // 订单总数
            long totalOrders = orderRepository.count();
            stats.put("totalOrders", totalOrders);
            
            // 用户总数
            long totalUsers = userRepository.count();
            stats.put("totalUsers", totalUsers);
            
            // 总收入（计算所有已支付订单的总金额）
            BigDecimal totalRevenue = orderRepository.findAll().stream()
                    .filter(order -> order.getStatus() == 2 || order.getStatus() == 3) // 已支付或已完成
                    .map(order -> order.getTotalPrice() != null ? order.getTotalPrice() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.put("totalRevenue", totalRevenue.doubleValue());
            
            return ResponseEntity.ok(ApiResponse.success("获取成功", stats));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("获取统计数据失败: " + e.getMessage()));
        }
    }

    /**
     * 获取所有管理员列表
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Admin>>> getAllAdmins() {
        try {
            List<Admin> admins = adminRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success("查询成功", admins));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取管理员
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Admin>> getAdminById(@PathVariable Integer id) {
        try {
            Admin admin = adminRepository.findById(id).orElse(null);
            if (admin == null) {
                return ResponseEntity.ok(ApiResponse.error("管理员不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success("查询成功", admin));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 更新管理员信息
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Admin>> updateAdmin(@RequestBody Admin admin) {
        try {
            Admin existingAdmin = adminRepository.findById(admin.getId()).orElse(null);
            if (existingAdmin == null) {
                return ResponseEntity.ok(ApiResponse.error("管理员不存在"));
            }
            
            if (admin.getEmail() != null) {
                existingAdmin.setEmail(admin.getEmail());
            }
            if (admin.getRealName() != null) {
                existingAdmin.setRealName(admin.getRealName());
            }
            if (admin.getPhone() != null) {
                existingAdmin.setPhone(admin.getPhone());
            }
            if (admin.getRole() != null) {
                existingAdmin.setRole(admin.getRole());
            }
            if (admin.getStatus() != null) {
                existingAdmin.setStatus(admin.getStatus());
            }
            if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
                // 使用BCrypt加密密码
                existingAdmin.setPassword(adminService.encodePassword(admin.getPassword()));
            }
            
            Admin savedAdmin = adminRepository.save(existingAdmin);
            return ResponseEntity.ok(ApiResponse.success("更新成功", savedAdmin));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("更新失败: " + e.getMessage()));
        }
    }

    /**
     * 删除管理员
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAdmin(@PathVariable Integer id) {
        try {
            Admin admin = adminRepository.findById(id).orElse(null);
            if (admin == null) {
                return ResponseEntity.ok(ApiResponse.error("管理员不存在"));
            }
            
            adminRepository.delete(admin);
            return ResponseEntity.ok(ApiResponse.success("删除成功"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("删除失败: " + e.getMessage()));
        }
    }
}