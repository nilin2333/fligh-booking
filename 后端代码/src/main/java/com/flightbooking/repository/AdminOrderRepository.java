package com.flightbooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flightbooking.entity.AdminOrder;

// 管理员端订单Repository接口

public interface AdminOrderRepository extends JpaRepository<AdminOrder, Long> {

    // 根据订单号查询订单
    Optional<AdminOrder> findByOrderNo(String orderNo);

    // 根据状态查询订单
    List<AdminOrder> findByStatus(Integer status);

    // 根据航班号查询订单
    List<AdminOrder> findByFlightNumber(String flightNumber);

    //根据乘客姓名查询订单
    List<AdminOrder> findByPassengerNameContaining(String passengerName);

    // 搜索订单（支持按航班号、订单号、乘客姓名搜索）
    @Query("SELECT o FROM AdminOrder o WHERE " +
           "o.flightNumber LIKE %:keyword% OR " +
           "o.orderNo LIKE %:keyword% OR " +
           "o.passengerName LIKE %:keyword%")
    List<AdminOrder> searchOrders(@Param("keyword") String keyword);

    // 根据用户ID查询订单
    List<AdminOrder> findByUserId(Integer userId);

    // 查询所有订单（按创建时间降序）
    List<AdminOrder> findAllByOrderByCreatedAtDesc();
}