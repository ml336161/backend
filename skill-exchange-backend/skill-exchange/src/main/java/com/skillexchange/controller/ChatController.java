package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.dto.CreateChatMessageRequest;
import com.skillexchange.service.ChatService;
import com.skillexchange.vo.ChatMessageVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Resource
    private ChatService chatService;

    @PostMapping("/send")
    public Result<ChatMessageVO> send(@RequestAttribute Long userId, @Valid @RequestBody CreateChatMessageRequest request) {
        ChatMessageVO message = chatService.send(userId, request);
        return Result.success(message);
    }

    @GetMapping("/conversation/{otherUserId}")
    public Result<List<ChatMessageVO>> getConversation(@RequestAttribute Long userId, @PathVariable Long otherUserId) {
        List<ChatMessageVO> messages = chatService.getConversation(userId, otherUserId);
        return Result.success(messages);
    }

    @GetMapping("/unread-count")
    public Result<Integer> getUnreadCount(@RequestAttribute Long userId) {
        int count = chatService.getUnreadCount(userId);
        return Result.success(count);
    }

    @PutMapping("/mark-read/{fromUserId}")
    public Result<Void> markAsRead(@RequestAttribute Long userId, @PathVariable Long fromUserId) {
        chatService.markAsRead(userId, fromUserId);
        return Result.success();
    }
}