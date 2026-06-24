package com.flightbooking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flightbooking.dto.request.LoginRequest;
import com.flightbooking.dto.request.RegisterRequest;
import com.flightbooking.dto.response.ApiResponse;
import com.flightbooking.dto.response.UserResponse;
import com.flightbooking.entity.User;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserResponse user = userService.register(request);
            return ResponseEntity.ok(ApiResponse.success("注册成功", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            UserResponse user = userService.login(request);
            return ResponseEntity.ok(ApiResponse.success("登录成功", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        try {
            UserResponse user = userService.getCurrentUser();
            return ResponseEntity.ok(ApiResponse.success("获取成功", user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        userService.logout();
        return ResponseEntity.ok(ApiResponse.success("登出成功"));
    }

    /**
     * 获取所有用户（管理员使用）
     * @param keyword 搜索关键字（可选，支持用户名、邮箱、手机号）
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(
            @RequestParam(required = false) String keyword) {
        try {
            List<User> users = userRepository.findAll();
            
            // 按关键字搜索（用户名、邮箱、手机号）
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchKeyword = keyword.trim().toLowerCase();
                users = users.stream()
                        .filter(user -> 
                            (user.getUsername() != null && user.getUsername().toLowerCase().contains(searchKeyword)) ||
                            (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchKeyword)) ||
                            (user.getPhone() != null && user.getPhone().toLowerCase().contains(searchKeyword))
                        )
                        .toList();
            }
            
            return ResponseEntity.ok(ApiResponse.success("查询成功", users));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取用户（管理员使用）
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Integer id) {
        try {
            User user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success("查询成功", user));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID更新用户信息
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> updateUserById(@PathVariable Integer id, @RequestBody java.util.Map<String, Object> updates) {
        try {
            User existingUser = userRepository.findById(id).orElse(null);
            if (existingUser == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }
            
            if (updates.containsKey("email")) {
                existingUser.setEmail((String) updates.get("email"));
            }
            if (updates.containsKey("phone")) {
                existingUser.setPhone((String) updates.get("phone"));
            }
            if (updates.containsKey("realName")) {
                existingUser.setRealName((String) updates.get("realName"));
            }
            
            User savedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(ApiResponse.success("更新成功", savedUser));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("更新失败: " + e.getMessage()));
        }
    }

    /**
     * 修改用户密码
     */
    @PutMapping("/users/{id}/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Integer id, @RequestBody java.util.Map<String, String> passwordData) {
        try {
            User existingUser = userRepository.findById(id).orElse(null);
            if (existingUser == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }
            
            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");
            
            if (oldPassword == null || newPassword == null) {
                return ResponseEntity.ok(ApiResponse.error("密码不能为空"));
            }
            
            // 验证旧密码（使用BCryptPasswordEncoder）
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
                return ResponseEntity.ok(ApiResponse.error("原密码不正确"));
            }
            
            // 更新密码（加密后存储）
            existingUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(existingUser);
            
            return ResponseEntity.ok(ApiResponse.success("密码修改成功", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("密码修改失败: " + e.getMessage()));
        }
    }

    /**
     * 更新用户信息（管理员使用）
     */
    @PutMapping("/users/update")
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestBody User user) {
        try {
            User existingUser = userRepository.findById(user.getId()).orElse(null);
            if (existingUser == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }
            
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPhone() != null) {
                existingUser.setPhone(user.getPhone());
            }
            if (user.getRealName() != null) {
                existingUser.setRealName(user.getRealName());
            }
            if (user.getStatus() != null) {
                existingUser.setStatus(user.getStatus());
            }
            
            User savedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(ApiResponse.success("更新成功", savedUser));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("更新失败: " + e.getMessage()));
        }
    }

    /**
     * 删除用户（管理员使用）
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Integer id) {
        try {
            User existingUser = userRepository.findById(id).orElse(null);
            if (existingUser == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }
            
            userRepository.delete(existingUser);
            return ResponseEntity.ok(ApiResponse.success("删除成功", "用户已注销"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("删除失败: " + e.getMessage()));
        }
    }
}