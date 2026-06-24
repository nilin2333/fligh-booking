package com.flightbooking.entity;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * 订单实体类
 */
@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_order_no", columnList = "order_no"),
    @Index(name = "idx_status", columnList = "status")
})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true, length = 50)
    private String orderNo;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "flight_id", nullable = false)
    private Long flightId;

    @Column(name = "flight_number", nullable = false, length = 20)
    private String flightNumber;

    @Column(name = "departure_city", nullable = false, length = 50)
    private String departureCity;

    @Column(name = "departure_code", length = 10)
    private String departureCode;

    @Column(name = "destination_city", nullable = false, length = 50)
    private String destinationCity;

    @Column(name = "destination_code", length = 10)
    private String destinationCode;

    @Column(name = "departure_date", nullable = false)
    private String departureDate;

    @Column(name = "departure_time", nullable = false)
    private Date departureTime;

    @Column(name = "arrival_time", nullable = false)
    private Date arrivalTime;

    @Column(name = "passenger_name", nullable = false, length = 50)
    private String passengerName;

    @Column(name = "passenger_id", nullable = false, length = 18)
    private String passengerId;

    @Column(name = "passenger_phone", nullable = false, length = 20)
    private String passengerPhone;

    @Column(name = "seat_preference", length = 20)
    private String seatPreference;

    @Column(name = "seat_number", length = 10)
    private String seatNumber;

    @Column(name = "duration", length = 20)
    private String duration;

    @Column(name = "cabin_type", nullable = false, length = 20)
    private String cabinType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "service_fee", precision = 10, scale = 2)
    private BigDecimal serviceFee;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private Integer status = 1; // 1-待支付, 2-已支付, 3-已出票, 4-已取消

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "buyer_username", length = 50)
    private String buyerUsername;

    // 支付方式
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    public Order() {}

    // Getters
    // 订单ID
    public Long getId() { return id; }
    // 订单号
    public String getOrderNo() { return orderNo; }
    // 用户ID
    public Integer getUserId() { return userId; }
    // 航班ID
    public Long getFlightId() { return flightId; }
    // 航班号
    public String getFlightNumber() { return flightNumber; }
    // 出发城市
    public String getDepartureCity() { return departureCity; }
    // 出发机场代码
    public String getDepartureCode() { return departureCode; }
    // 目的地城市
    public String getDestinationCity() { return destinationCity; }
    // 目的地机场代码
    public String getDestinationCode() { return destinationCode; }
    // 出发时间
    public Date getDepartureTime() { return departureTime; }
    // 到达时间
    public Date getArrivalTime() { return arrivalTime; }
    // 乘客姓名
    public String getPassengerName() { return passengerName; }
    // 乘客身份证号
    public String getPassengerId() { return passengerId; }
    // 乘客手机号
    public String getPassengerPhone() { return passengerPhone; }
    // 座位类型
    public String getSeatPreference() { return seatPreference; }
    // 座位号
    public String getSeatNumber() { return seatNumber; }
    // 飞行时间
    public String getDuration() { return duration; }
    // 舱位类型
    public String getCabinType() { return cabinType; }
    // 获取出发日期
    public String getDepartureDate() { return departureDate; }
    // 购买者用户名
    public String getBuyerUsername() { return buyerUsername; }
    // 支付方式
    public String getPaymentMethod() { return paymentMethod; }
    // 票价
    public BigDecimal getPrice() { return price; }
    // 服务费
    public BigDecimal getServiceFee() { return serviceFee; }
    // 总金额
    public BigDecimal getTotalPrice() { return totalPrice; }
    // 订单状态
    public Integer getStatus() { return status; }
    // 创建时间
    public Date getCreatedAt() { return createdAt; }
    // 更新时间
    public Date getUpdatedAt() { return updatedAt; }


    // Setters
    // 订单ID
    public void setId(Long id) { this.id = id; }
    // 订单号
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    // 用户ID
    public void setUserId(Integer userId) { this.userId = userId; }
    // 航班ID
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    // 航班号
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    // 出发城市
    public void setDepartureCity(String departureCity) { this.departureCity = departureCity; }
    // 出发机场代码
    public void setDepartureCode(String departureCode) { this.departureCode = departureCode; }
    // 目的地城市
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }
    // 目的地机场代码
    public void setDestinationCode(String destinationCode) { this.destinationCode = destinationCode; }
    // 出发时间
    public void setDepartureTime(Date departureTime) { this.departureTime = departureTime; }
    // 到达时间
    public void setArrivalTime(Date arrivalTime) { this.arrivalTime = arrivalTime; }
    // 乘客姓名
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    // 乘客身份证号
    public void setPassengerId(String passengerId) { this.passengerId = passengerId; }
    // 乘客手机号
    public void setPassengerPhone(String passengerPhone) { this.passengerPhone = passengerPhone; }
    // 乘客座位偏好
    public void setSeatPreference(String seatPreference) { this.seatPreference = seatPreference; }
    // 乘客座位号
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    // 飞行时间长
    public void setDuration(String duration) { this.duration = duration; }
    // 乘客舱位类型
    public void setCabinType(String cabinType) { this.cabinType = cabinType; }
    // 票价
    public void setPrice(BigDecimal price) { this.price = price; }
    // 服务费
    public void setServiceFee(BigDecimal serviceFee) { this.serviceFee = serviceFee; }
    // 总金额
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    // 订单状态
    public void setStatus(Integer status) { this.status = status; }
    // 创建时间
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    // 更新时间
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    // 设置购买者用户名
    public void setBuyerUsername(String buyerUsername) { this.buyerUsername = buyerUsername; }
    // 设置出发日期
    public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }
    // 设置支付方式
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    
    // 创建订单时，自动生成订单号
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        if (orderNo == null || orderNo.isEmpty()) {
            orderNo = "ORD" + System.currentTimeMillis();
        }
    }
    // 更新订单状态
    @PreUpdate
    protected void onUpdate() { updatedAt = new Date(); }
    



}