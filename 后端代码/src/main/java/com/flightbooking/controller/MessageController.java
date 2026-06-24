package com.flightbooking.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.flightbooking.entity.Message;
import com.flightbooking.service.MessageService;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Message>>> getMessages(
            @RequestParam Integer userId,
            @RequestParam(required = false) String type) {
        try {
            List<Message> messages;
            if ("unread".equals(type)) {
                messages = messageService.getUnreadMessages(userId);
            } else if ("ORDER".equals(type) || "SYSTEM".equals(type) || "ACCOUNT".equals(type)) {
                messages = messageService.getMessagesByType(userId, type);
            } else {
                messages = messageService.getMessagesByUserId(userId);
            }
            return ResponseEntity.ok(ApiResponse.success("查询成功", messages));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询失败: " + e.getMessage()));
        }
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUnreadCount(@RequestParam Integer userId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("unreadCount", messageService.getUnreadCount(userId));
            return ResponseEntity.ok(ApiResponse.success("查询成功", result));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("查询失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id) {
        try {
            messageService.markAsRead(id);
            return ResponseEntity.ok(ApiResponse.success("已标记为已读"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("操作失败: " + e.getMessage()));
        }
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(@RequestParam Integer userId) {
        try {
            messageService.markAllAsRead(userId);
            return ResponseEntity.ok(ApiResponse.success("已全部标记为已读"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("操作失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long id) {
        try {
            messageService.deleteMessage(id);
            return ResponseEntity.ok(ApiResponse.success("删除成功"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("删除失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<Void>> deleteMessages(@RequestBody List<Long> ids) {
        try {
            messageService.deleteMessages(ids);
            return ResponseEntity.ok(ApiResponse.success("批量删除成功"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("批量删除失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/read")
    public ResponseEntity<ApiResponse<Void>> deleteReadMessages(@RequestParam Integer userId) {
        try {
            messageService.deleteReadMessages(userId);
            return ResponseEntity.ok(ApiResponse.success("已删除所有已读消息"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("操作失败: " + e.getMessage()));
        }
    }
}