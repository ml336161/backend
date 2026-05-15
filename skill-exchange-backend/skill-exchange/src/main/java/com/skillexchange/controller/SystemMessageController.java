package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.entity.SystemMessage;
import com.skillexchange.service.SystemMessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/system-message")
public class SystemMessageController {

    @Resource
    private SystemMessageService systemMessageService;

    @GetMapping
    public Result<List<SystemMessage>> getMessages(@RequestAttribute Long userId) {
        List<SystemMessage> messages = systemMessageService.getMessages(userId);
        systemMessageService.markAllRead(userId);
        return Result.success(messages);
    }

    @GetMapping("/unread-count")
    public Result<Integer> getUnreadCount(@RequestAttribute Long userId) {
        int count = systemMessageService.getUnreadCount(userId);
        return Result.success(count);
    }

    @PostMapping("/mark-read")
    public Result<Void> markAllRead(@RequestAttribute Long userId) {
        systemMessageService.markAllRead(userId);
        return Result.success();
    }
}
