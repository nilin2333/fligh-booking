package com.flightbooking.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.flightbooking.entity.Message;
import com.flightbooking.repository.MessageRepository;
import com.flightbooking.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(Integer userId, String title, String content, String type, String orderNo) {
        Message message = new Message();
        message.setUserId(userId);
        message.setTitle(title);
        message.setContent(content);
        message.setType(type);
        message.setOrderNo(orderNo);
        message.setIsRead(false);
        message.setCreatedAt(new Date());
        
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByUserId(Integer userId) {
        return messageRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Message> getUnreadMessages(Integer userId) {
        return messageRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false);
    }

    @Override
    public List<Message> getMessagesByType(Integer userId, String type) {
        return messageRepository.findByUserIdAndTypeOrderByCreatedAtDesc(userId, type);
    }

    @Override
    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message != null) {
            message.setIsRead(true);
            messageRepository.save(message);
        }
    }

    @Override
    public void markAllAsRead(Integer userId) {
        List<Message> messages = messageRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false);
        messages.forEach(m -> m.setIsRead(true));
        messageRepository.saveAll(messages);
    }

    @Override
    public int getUnreadCount(Integer userId) {
        return messageRepository.countByUserIdAndIsRead(userId, false);
    }

    @Override
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public void deleteMessages(List<Long> ids) {
        messageRepository.deleteAllById(ids);
    }

    @Override
    public void deleteReadMessages(Integer userId) {
        List<Message> readMessages = messageRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, true);
        readMessages.forEach(m -> messageRepository.deleteById(m.getId()));
    }
}