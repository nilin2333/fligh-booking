package com.flightbooking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flightbooking.dto.response.ApiResponse;
import com.flightbooking.entity.AdminOrder;
import com.flightbooking.entity.Order;
import com.flightbooking.repository.AdminOrderRepository;
import com.flightbooking.repository.OrderRepository;

/**
 * 管理员端订单控制器
 */
@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class AdminOrderController {

    private final AdminOrderRepository adminOrderRepository;
    private final OrderRepository orderRepository;

    public AdminOrderController(AdminOrderRepository adminOrderRepository, OrderRepository orderRepository) {
        this.adminOrderRepository = adminOrderRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * 获取所有订单（管理员使用）
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AdminOrder>>> getAllOrders(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        try {
            List<AdminOrder> orders = adminOrderRepository.findAllByOrderByCreatedAtDesc();
            
            // 按状态筛选
            if (status != null) {
                orders = orders.stream()
                        .filter(order -> status.equals(order.getStatus()))
                        .toList();
            }
            
            // 按关键字搜索（航班号、订单号、乘客姓名）
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchKeyword = keyword.trim().toLowerCase();
                orders = orders.stream()
                        .filter(order -> 
                            (order.getFlightNumber() != null && order.getFlightNumber().toLowerCase().contains(searchKeyword)) ||
                            (order.getOrderNo() != null && order.getOrderNo().toLowerCase().contains(searchKeyword)) ||
                            (order.getPassengerName() != null && order.getPassengerName().toLowerCase().contains(searchKeyword))
                        )
                        .toList();
            }
            
            return ResponseEntity.ok(ApiResponse.success("查询成功", orders));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据订单ID查询订单（管理员使用）
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminOrder>> getOrderById(@PathVariable Long id) {
        try {
            AdminOrder order = adminOrderRepository.findById(id).orElse(null);
            if (order == null) {
                return ResponseEntity.ok(ApiResponse.error("订单不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success("查询成功", order));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询订单失败: " + e.getMessage()));
        }
    }

    /**
     * 删除订单（管理员使用）- 根据订单号删除
     * 只删除管理员端的订单记录，不影响用户端订单
     */
    @DeleteMapping("/delete/{orderNo}")
    public ResponseEntity<ApiResponse<String>> deleteOrderByOrderNo(@PathVariable String orderNo) {
        try {
            AdminOrder order = adminOrderRepository.findByOrderNo(orderNo).orElse(null);
            if (order == null) {
                return ResponseEntity.ok(ApiResponse.error("订单不存在"));
            }

            adminOrderRepository.delete(order);
            return ResponseEntity.ok(ApiResponse.success("删除成功", "订单记录已删除"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("删除失败: " + e.getMessage()));
        }
    }

    /**
     * 更新订单（管理员使用）
     * 同时更新管理员端和用户端的订单数据
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<String>> updateOrder(@RequestBody AdminOrder orderData) {
        try {
            if (orderData.getId() == null) {
                return ResponseEntity.ok(ApiResponse.error("订单ID不能为空"));
            }

            AdminOrder existingOrder = adminOrderRepository.findById(orderData.getId()).orElse(null);
            if (existingOrder == null) {
                return ResponseEntity.ok(ApiResponse.error("订单不存在"));
            }

            // 更新管理员端订单字段
            if (orderData.getFlightNumber() != null) {
                existingOrder.setFlightNumber(orderData.getFlightNumber());
            }
            if (orderData.getPassengerName() != null) {
                existingOrder.setPassengerName(orderData.getPassengerName());
            }
            if (orderData.getPassengerId() != null) {
                existingOrder.setPassengerId(orderData.getPassengerId());
            }
            if (orderData.getPassengerPhone() != null) {
                existingOrder.setPassengerPhone(orderData.getPassengerPhone());
            }
            if (orderData.getStatus() != null) {
                existingOrder.setStatus(orderData.getStatus());
            }
            if (orderData.getPaymentMethod() != null) {
                existingOrder.setPaymentMethod(orderData.getPaymentMethod());
            }

            adminOrderRepository.save(existingOrder);

            // 同步更新用户端订单表（通过订单号查找）
            if (existingOrder.getOrderNo() != null) {
                Order userOrder = orderRepository.findByOrderNo(existingOrder.getOrderNo());
                if (userOrder != null) {
                    // 更新相同的字段
                    if (orderData.getFlightNumber() != null) {
                        userOrder.setFlightNumber(orderData.getFlightNumber());
                    }
                    if (orderData.getPassengerName() != null) {
                        userOrder.setPassengerName(orderData.getPassengerName());
                    }
                    if (orderData.getPassengerId() != null) {
                        userOrder.setPassengerId(orderData.getPassengerId());
                    }
                    if (orderData.getPassengerPhone() != null) {
                        userOrder.setPassengerPhone(orderData.getPassengerPhone());
                    }
                    if (orderData.getStatus() != null) {
                        userOrder.setStatus(orderData.getStatus());
                    }
                    if (orderData.getPaymentMethod() != null) {
                        userOrder.setPaymentMethod(orderData.getPaymentMethod());
                    }

                    orderRepository.save(userOrder);
                }
            }

            return ResponseEntity.ok(ApiResponse.success("订单更新成功", "订单已更新"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("更新失败: " + e.getMessage()));
        }
    }
}