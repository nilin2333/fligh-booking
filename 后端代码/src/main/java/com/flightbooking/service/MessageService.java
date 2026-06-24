// MessageService.java
package com.flightbooking.service;

import java.util.List;

import com.flightbooking.entity.Message;

public interface MessageService {
    Message createMessage(Integer userId, String title, String content, String type, String orderNo);
    List<Message> getMessagesByUserId(Integer userId);
    List<Message> getUnreadMessages(Integer userId);
    List<Message> getMessagesByType(Integer userId, String type);
    void markAsRead(Long messageId);
    void markAllAsRead(Integer userId);
    int getUnreadCount(Integer userId);
    void deleteMessage(Long id);
    void deleteMessages(List<Long> ids);
    void deleteReadMessages(Integer userId);
}