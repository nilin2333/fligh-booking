package com.flightbooking.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
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

import com.flightbooking.dto.response.ApiResponse;
import com.flightbooking.entity.Order;
import com.flightbooking.entity.User;
import com.flightbooking.repository.OrderRepository;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.service.OrderService;

//订单控制器
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, UserRepository userRepository, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    //创建订单
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createOrder(@RequestBody Order order) {
        try {
            // 生成订单号
            if (order.getOrderNo() == null || order.getOrderNo().isEmpty()) {
                order.setOrderNo("ORD" + System.currentTimeMillis());
            }
            
            Order savedOrder = orderService.createOrder(order);
            
            Map<String, Object> result = new HashMap<>();
            result.put("orderId", savedOrder.getId());
            result.put("orderNo", savedOrder.getOrderNo());
            
            return ResponseEntity.ok(ApiResponse.success("订单创建成功", result));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("订单创建失败: " + e.getMessage()));
        }
    }

    // 根据用户名查询订单列表（支持两种路径）
    @GetMapping({"/user", ""})
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByUsername(@RequestParam String username) {
        try {
            // 先检查用户是否存在
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                // 用户不存在时，返回空列表（允许匿名浏览或新用户）
                return ResponseEntity.ok(ApiResponse.success("查询成功", java.util.Collections.emptyList()));
            }
            User user = userOpt.get();
            
            // 查询订单
            List<Order> orders = orderService.getOrdersByUserId(user.getId());
            return ResponseEntity.ok(ApiResponse.success("查询成功", orders));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询订单失败: " + e.getMessage()));
        }
    }

    //根据购买者用户名查询订单列表
    @GetMapping("/buyer/{username}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByBuyerUsername(@PathVariable String username) {
        try {
            List<Order> orders = orderService.getOrdersByBuyerUsername(username);
            return ResponseEntity.ok(ApiResponse.success("查询成功", orders));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询订单失败: " + e.getMessage()));
        }
    }

    //根据乘客姓名查询订单列表
    @GetMapping("/passenger/{name}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByPassengerName(@PathVariable String name) {
        try {
            List<Order> orders = orderService.getOrdersByPassengerName(name);
            return ResponseEntity.ok(ApiResponse.success("查询成功", orders));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询订单失败: " + e.getMessage()));
        }
    }

    //根据订单号查询订单
    @GetMapping("/{orderNo}")
    public ResponseEntity<ApiResponse<Order>> getOrderByOrderNo(@PathVariable String orderNo) {
        try {
            Order order = orderService.getOrderByOrderNo(orderNo);
            if (order == null) {
                return ResponseEntity.ok(ApiResponse.error("订单不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success("查询成功", order));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询订单失败: " + e.getMessage()));
        }
    }

    //根据订单ID查询订单（管理员使用）
    @GetMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderByOrderId(id);
            if (order == null) {
                return ResponseEntity.ok(ApiResponse.error("订单不存在"));
            }
            return ResponseEntity.ok(ApiResponse.success("查询成功", order));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询订单失败: " + e.getMessage()));
        }
    }

    //更新订单信息（管理员使用）
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Order>> updateOrder(@RequestBody Order order) {
        try {
            Order existingOrder = orderService.getOrderByOrderId(order.getId());
            if (existingOrder == null) {
                return ResponseEntity.ok(ApiResponse.error("订单不存在"));
            }
            
            // 更新可编辑字段
            if (order.getFlightNumber() != null) {
                existingOrder.setFlightNumber(order.getFlightNumber());
            }
            if (order.getPassengerName() != null) {
                existingOrder.setPassengerName(order.getPassengerName());
            }
            if (order.getPassengerId() != null) {
                existingOrder.setPassengerId(order.getPassengerId());
            }
            if (order.getPassengerPhone() != null) {
                existingOrder.setPassengerPhone(order.getPassengerPhone());
            }
            if (order.getStatus() != null) {
                existingOrder.setStatus(order.getStatus());
            }
            if (order.getPaymentMethod() != null) {
                existingOrder.setPaymentMethod(order.getPaymentMethod());
            }
            
            Order savedOrder = orderService.save(existingOrder);
            return ResponseEntity.ok(ApiResponse.success("更新成功", savedOrder));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("更新订单失败: " + e.getMessage()));
        }
    }

    //更新订单状态
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable Long orderId, 
            @RequestBody Map<String, Integer> request) {
        try {
            Integer status = request.get("status");
            if (status == null) {
                return ResponseEntity.ok(ApiResponse.error("请提供状态值"));
            }
            
            Order order = orderService.updateStatus(orderId, status);
            if (order == null) {
                return ResponseEntity.ok(ApiResponse.error("订单不存在"));
            }
        
            return ResponseEntity.ok(ApiResponse.success("状态更新成功", order));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("更新状态失败: " + e.getMessage()));
        }
    }

    //改签航班
    @PutMapping("/{orderId}/change")
    public ResponseEntity<ApiResponse<Order>> changeFlight(@PathVariable Long orderId, @RequestBody Map<String, Object> request) {
        try {
            Order order = orderService.getOrderByOrderId(orderId);
            if (order == null) {
                return ResponseEntity.ok(ApiResponse.error("订单不存在"));
            }
        
            if (order.getStatus() != 2) { // 只有已确认状态才能改签
                return ResponseEntity.ok(ApiResponse.error("订单状态不允许改签"));
            }
        
            // 获取新航班信息
            String newFlightNumber = (String) request.get("newFlightNumber");
            String newDepartureDate = (String) request.get("newDepartureDate"); // 新增日期字段
            String newDepartureTimeStr = (String) request.get("newDepartureTime");
            String newArrivalTimeStr = (String) request.get("newArrivalTime");
        
            Integer priceInt = (Integer) request.get("newPrice");
            BigDecimal newPrice = priceInt != null ? BigDecimal.valueOf(priceInt) : BigDecimal.ZERO;
        
            // 更新订单信息
            order.setFlightNumber(newFlightNumber);
        
            // 处理日期和时间
            if (newDepartureDate != null && !newDepartureDate.isEmpty()) {
                // 如果有新日期，更新日期
                order.setDepartureDate(newDepartureDate);
            }
        
            Date newDepartureTime = updateTime(order.getDepartureTime(), newDepartureTimeStr);
            Date newArrivalTime = updateTime(order.getArrivalTime(), newArrivalTimeStr);
        
            order.setDepartureTime(newDepartureTime);
            order.setArrivalTime(newArrivalTime);
            order.setPrice(newPrice);
        
            BigDecimal serviceFee = order.getServiceFee() != null ? order.getServiceFee() : BigDecimal.valueOf(20);
            order.setTotalPrice(newPrice.add(serviceFee));
        
            // 保存更新
            order = orderService.createOrder(order);
        
            return ResponseEntity.ok(ApiResponse.success("改签成功", order));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("改签失败: " + e.getMessage()));
        }
    }

// 更新时间部分，保留日期
    private Date updateTime(Date originalDate, String timeStr) {
        if (originalDate == null || timeStr == null || timeStr.isEmpty()) {
            return originalDate;
        }
    
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(originalDate);
        
            String[] timeParts = timeStr.split(":");
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);
        
            cal.set(Calendar.HOUR_OF_DAY, hours);
            cal.set(Calendar.MINUTE, minutes);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        
            return cal.getTime();
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return originalDate;
        }
    }


    //支付订单
    @PutMapping("/pay")
    public ResponseEntity<ApiResponse<Order>> payOrder(@RequestBody Map<String, Object> request) {
        try {
        // 获取订单号
        String orderNo = (String) request.get("orderNo");
        if (orderNo == null || orderNo.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error("订单号不能为空"));
        }

        // 按订单号查询订单
        Order order = orderService.getOrderByOrderNo(orderNo);
        if (order == null) {
            return ResponseEntity.ok(ApiResponse.error("订单不存在"));
        }
        
        // 检查订单状态
        if (order.getStatus() != 1) { // 1表示待支付
            return ResponseEntity.ok(ApiResponse.error("订单状态不允许支付"));
        }
        
        //记录支付方式
        String paymentMethod = (String) request.get("paymentMethod");
        order.setPaymentMethod(paymentMethod);
        
        // 更新订单状态为已支付
        order.setStatus(2); // 2表示已确认
        
        // 保存订单（包含支付方式和状态）
        order = orderService.save(order);
        
        return ResponseEntity.ok(ApiResponse.success("支付成功", order));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("支付失败: " + e.getMessage()));
        }
    }
    
    //删除订单
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable Long orderId) {
        try {
            if (!orderService.existsById(orderId)) {
                return ResponseEntity.ok(ApiResponse.error("订单不存在"));
            }

            orderService.deleteOrder(orderId);
            return ResponseEntity.ok(ApiResponse.success("删除成功", "订单记录已删除"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("删除失败: " + e.getMessage()));
        }
    }

    /**
     * 获取所有订单（管理员使用）
     * @param status 订单状态筛选（可选）
     * @param keyword 搜索关键字（可选，支持航班号、订单号、乘客姓名）
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        try {
            List<Order> orders = orderRepository.findAll();
            
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
}