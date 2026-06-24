package com.flightbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightbooking.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    //根据用户ID查询订单列表
    List<Order> findByUserIdOrderByCreatedAtDesc(Integer userId);

    //根据购买者用户名查询订单列表
    List<Order> findByBuyerUsernameOrderByCreatedAtDesc(String buyerUsername);

    //根据乘客姓名查询订单列表
    List<Order> findByPassengerNameOrderByCreatedAtDesc(String passengerName);

    //根据订单号查询订单
    Order findByOrderNo(String orderNo);


}