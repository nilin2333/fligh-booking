package com.flightbooking.service;

import java.util.List;

import com.flightbooking.entity.Order;

// 订单服务接口
public interface OrderService {

    // 创建订单
    Order createOrder(Order order);

    // 根据用户ID查询订单列表
    List<Order> getOrdersByUserId(Integer userId);  // 添加这行
    
    // 根据购买者用户名查询订单列表
    List<Order> getOrdersByBuyerUsername(String username);

    // 根据乘客姓名查询订单列表
    List<Order> getOrdersByPassengerName(String passengerName);

    //根据订单号查询订单
    Order getOrderByOrderNo(String orderNo);

    // 根据订单ID查询订单
    Order getOrderByOrderId(Long orderId);

    //更新订单状态
    Order updateStatus(Long orderId, Integer status);

    // 保存订单
    Order save(Order order);

    //删除订单
    void deleteOrder(Long orderId);
    boolean existsById(Long orderId);
}