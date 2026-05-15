package com.skillexchange.service;

import com.skillexchange.entity.SystemMessage;

import java.util.List;

public interface SystemMessageService {

    void sendMessage(Long userId, String content, Integer type);

    void sendWelcomeMessage(Long userId);

    void sendFeedbackReply(Long userId, String content);

    List<SystemMessage> getMessages(Long userId);

    int getUnreadCount(Long userId);

    void markAllRead(Long userId);
}
