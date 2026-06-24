package com.flightbooking.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightbooking.entity.AdminOrder;
import com.flightbooking.entity.Order;
import com.flightbooking.repository.AdminOrderRepository;
import com.flightbooking.repository.OrderRepository;
import com.flightbooking.repository.UserRepository;
import com.flightbooking.service.MessageService;
import com.flightbooking.service.OrderService;

//订单服务实现类
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final MessageService messageService;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, 
        AdminOrderRepository adminOrderRepository,
        MessageService messageService,
        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.adminOrderRepository = adminOrderRepository;
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        // 调试日志：打印接收到的订单信息
        System.out.println("创建订单: buyerUsername=" + order.getBuyerUsername() + ", userId=" + order.getUserId());
        
        Order savedOrder = orderRepository.save(order);
        
        // 同步创建管理员端订单
        AdminOrder adminOrder = new AdminOrder(savedOrder);
        adminOrderRepository.save(adminOrder);
        
        // 发送订单创建消息，提醒及时支付
        String title = "订单已创建";
        String content = String.format("您的订单 %s 已创建成功！\n\n航班号: %s\n出发地: %s → 目的地: %s\n出发时间: %s\n请及时完成支付，以免影响您的出行。",
                savedOrder.getOrderNo(),
                savedOrder.getFlightNumber(),
                savedOrder.getDepartureCity(),
                savedOrder.getDestinationCity(),
                formatDate(savedOrder.getDepartureTime()));
        // 使用购买者用户名获取用户ID，确保消息发送给正确的用户
        Integer buyerUserId = getUserIdByUsername(savedOrder.getBuyerUsername());
        if (buyerUserId != null) {
            messageService.createMessage(buyerUserId, title, content, "ORDER", savedOrder.getOrderNo());
            System.out.println("订单创建消息已发送，orderNo=" + savedOrder.getOrderNo() + ", buyerUsername=" + savedOrder.getBuyerUsername() + ", userId=" + buyerUserId);
        } else {
            System.out.println("订单创建消息发送失败：无法找到购买者用户ID，buyerUsername=" + savedOrder.getBuyerUsername());
        }
        
        return savedOrder;
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Order> getOrdersByBuyerUsername(String username) {
        return orderRepository.findByBuyerUsernameOrderByCreatedAtDesc(username);
    }

    @Override
    public List<Order> getOrdersByPassengerName(String passengerName) {
        return orderRepository.findByPassengerNameOrderByCreatedAtDesc(passengerName);
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    @Override
    @Transactional
    public Order updateStatus(Long orderId, Integer status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            order = orderRepository.save(order);
            
            // 同步更新管理员端订单
            AdminOrder adminOrder = adminOrderRepository.findByOrderNo(order.getOrderNo()).orElse(null);
            if (adminOrder != null) {
                adminOrder.updateFromOrder(order);
                adminOrderRepository.save(adminOrder);
            }
            
            // 根据状态变化发送消息
            sendStatusChangeMessage(order, status);
        }
        return order;
    }
    
    /**
     * 根据订单状态变化发送消息（使用规则switch）
     */
    private void sendStatusChangeMessage(Order order, Integer newStatus) {
        // 状态说明: 1-待支付, 2-已支付, 3-已完成, 4-已取消, 5-已过期
        var message = switch (newStatus) {
            case 1 -> null; // 待支付 - 订单刚创建，createOrder已处理
            case 2 -> new MessageInfo(
                "预订成功",
                String.format("恭喜！您的订单 %s 已预订成功！\n\n航班号: %s\n出发地: %s → 目的地: %s\n出发时间: %s\n请按时前往机场办理登机手续，祝您旅途愉快！",
                    order.getOrderNo(), order.getFlightNumber(),
                    order.getDepartureCity(), order.getDestinationCity(), formatDate(order.getDepartureTime()))
            );
            case 3 -> new MessageInfo(
                "订单已完成",
                String.format("您的订单 %s 已完成！\n\n感谢您选择我们的服务，期待下次再见！\n\n航班号: %s\n出发地: %s → 目的地: %s",
                    order.getOrderNo(),
                    order.getFlightNumber(),
                    order.getDepartureCity(),
                    order.getDestinationCity())
            );
            case 4 -> new MessageInfo(
                "订单已取消",
                String.format("您的订单 %s 已取消。\n\n如有疑问，请联系客服。", order.getOrderNo())
            );
            case 5 -> new MessageInfo(
                "订单已过期",
                String.format("抱歉，您的订单 %s 已过期。\n\n订单号: %s\n航班号: %s\n请重新预订。",
                    order.getOrderNo(), order.getOrderNo(), order.getFlightNumber())
            );
            default -> null;
        };
        
        if (message != null) {
            // 使用购买者用户名获取用户ID，确保消息发送给正确的用户
            Integer buyerUserId = getUserIdByUsername(order.getBuyerUsername());
            if (buyerUserId != null) {
                messageService.createMessage(buyerUserId, message.title, message.content, "ORDER", order.getOrderNo());
            }
        }
    }
    
    /**
     * 格式化日期为友好格式
     */
    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    
    /**
     * 根据用户名获取用户ID
     * @param username 用户名
     * @return 用户ID，如果用户不存在返回null
     */
    private Integer getUserIdByUsername(String username) {
        if (username == null || username.isEmpty()) {
            System.out.println("getUserIdByUsername: username为空");
            return null;
        }
        return userRepository.findByUsername(username)
                .map(user -> {
                    System.out.println("getUserIdByUsername: username=" + username + ", userId=" + user.getId());
                    return user.getId();
                })
                .orElseGet(() -> {
                    System.out.println("getUserIdByUsername: 用户不存在，username=" + username);
                    return null;
                });
    }
    
    /**
     * 消息信息记录类
     */
    private record MessageInfo(String title, String content) {}

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        // 用户端删除订单时，只删除用户端订单表（管理员端订单由管理员自己删除）
        // 删除前先获取订单信息，以便发送消息
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            // 发送订单删除消息
            String title = "订单已删除";
            String content = String.format("您的订单 %s 已成功删除。\n\n如有需要，请重新预订。", order.getOrderNo());
            Integer buyerUserId = getUserIdByUsername(order.getBuyerUsername());
            if (buyerUserId != null) {
                messageService.createMessage(buyerUserId, title, content, "ORDER", order.getOrderNo());
                System.out.println("订单删除消息已发送，orderNo=" + order.getOrderNo() + ", buyerUsername=" + order.getBuyerUsername() + ", userId=" + buyerUserId);
            } else {
                System.out.println("订单删除消息发送失败：无法找到购买者用户ID，buyerUsername=" + order.getBuyerUsername());
            }
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public boolean existsById(Long orderId) {
        return orderRepository.existsById(orderId);
    }

    @Override
    public Order getOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    @Transactional
    public Order save(Order order) {
        Order savedOrder = orderRepository.save(order);
        
        // 同步更新管理员端订单（除删除操作外，其他更新都同步）
        AdminOrder adminOrder = adminOrderRepository.findByOrderNo(savedOrder.getOrderNo()).orElse(null);
        if (adminOrder != null) {
            adminOrder.updateFromOrder(savedOrder);
            adminOrderRepository.save(adminOrder);
        }
        
        return savedOrder;
    }

}