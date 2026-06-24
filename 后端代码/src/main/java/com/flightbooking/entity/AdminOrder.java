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
 * 管理员端订单实体类
 * 与用户端订单信息同步
 */
@Entity
@Table(name = "admin_orders", indexes = {
    @Index(name = "idx_admin_order_no", columnList = "order_no"),
    @Index(name = "idx_admin_status", columnList = "status"),
    @Index(name = "idx_admin_flight_number", columnList = "flight_number")
})
public class AdminOrder {

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
    private Integer status = 1; // 1-待支付, 2-已支付, 3-已完成, 4-已取消, 5-已过期

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "buyer_username", length = 50)
    private String buyerUsername;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    // 管理员备注
    @Column(name = "admin_note", length = 500)
    private String adminNote;

    public AdminOrder() {}

    // 从用户端订单复制数据
    public AdminOrder(Order order) {
        this.orderNo = order.getOrderNo();
        this.userId = order.getUserId();
        this.flightId = order.getFlightId();
        this.flightNumber = order.getFlightNumber();
        this.departureCity = order.getDepartureCity();
        this.departureCode = order.getDepartureCode();
        this.destinationCity = order.getDestinationCity();
        this.destinationCode = order.getDestinationCode();
        this.departureDate = order.getDepartureDate();
        this.departureTime = order.getDepartureTime();
        this.arrivalTime = order.getArrivalTime();
        this.passengerName = order.getPassengerName();
        this.passengerId = order.getPassengerId();
        this.passengerPhone = order.getPassengerPhone();
        this.seatPreference = order.getSeatPreference();
        this.seatNumber = order.getSeatNumber();
        this.duration = order.getDuration();
        this.cabinType = order.getCabinType();
        this.price = order.getPrice();
        this.serviceFee = order.getServiceFee();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
        this.buyerUsername = order.getBuyerUsername();
        this.paymentMethod = order.getPaymentMethod();
    }

    // 更新数据（从用户端订单同步）
    public void updateFromOrder(Order order) {
        this.userId = order.getUserId();
        this.flightId = order.getFlightId();
        this.flightNumber = order.getFlightNumber();
        this.departureCity = order.getDepartureCity();
        this.departureCode = order.getDepartureCode();
        this.destinationCity = order.getDestinationCity();
        this.destinationCode = order.getDestinationCode();
        this.departureDate = order.getDepartureDate();
        this.departureTime = order.getDepartureTime();
        this.arrivalTime = order.getArrivalTime();
        this.passengerName = order.getPassengerName();
        this.passengerId = order.getPassengerId();
        this.passengerPhone = order.getPassengerPhone();
        this.seatPreference = order.getSeatPreference();
        this.seatNumber = order.getSeatNumber();
        this.duration = order.getDuration();
        this.cabinType = order.getCabinType();
        this.price = order.getPrice();
        this.serviceFee = order.getServiceFee();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.updatedAt = order.getUpdatedAt();
        this.buyerUsername = order.getBuyerUsername();
        this.paymentMethod = order.getPaymentMethod();
    }

    // Getters
    public Long getId() { return id; }
    public String getOrderNo() { return orderNo; }
    public Integer getUserId() { return userId; }
    public Long getFlightId() { return flightId; }
    public String getFlightNumber() { return flightNumber; }
    public String getDepartureCity() { return departureCity; }
    public String getDepartureCode() { return departureCode; }
    public String getDestinationCity() { return destinationCity; }
    public String getDestinationCode() { return destinationCode; }
    public String getDepartureDate() { return departureDate; }
    public Date getDepartureTime() { return departureTime; }
    public Date getArrivalTime() { return arrivalTime; }
    public String getPassengerName() { return passengerName; }
    public String getPassengerId() { return passengerId; }
    public String getPassengerPhone() { return passengerPhone; }
    public String getSeatPreference() { return seatPreference; }
    public String getSeatNumber() { return seatNumber; }
    public String getDuration() { return duration; }
    public String getCabinType() { return cabinType; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getServiceFee() { return serviceFee; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public Integer getStatus() { return status; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public String getBuyerUsername() { return buyerUsername; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getAdminNote() { return adminNote; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public void setDepartureCity(String departureCity) { this.departureCity = departureCity; }
    public void setDepartureCode(String departureCode) { this.departureCode = departureCode; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }
    public void setDestinationCode(String destinationCode) { this.destinationCode = destinationCode; }
    public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }
    public void setDepartureTime(Date departureTime) { this.departureTime = departureTime; }
    public void setArrivalTime(Date arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public void setPassengerId(String passengerId) { this.passengerId = passengerId; }
    public void setPassengerPhone(String passengerPhone) { this.passengerPhone = passengerPhone; }
    public void setSeatPreference(String seatPreference) { this.seatPreference = seatPreference; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setCabinType(String cabinType) { this.cabinType = cabinType; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setServiceFee(BigDecimal serviceFee) { this.serviceFee = serviceFee; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public void setStatus(Integer status) { this.status = status; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public void setBuyerUsername(String buyerUsername) { this.buyerUsername = buyerUsername; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        if (orderNo == null || orderNo.isEmpty()) {
            orderNo = "ORD" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() { 
        updatedAt = new Date(); 
    }
}