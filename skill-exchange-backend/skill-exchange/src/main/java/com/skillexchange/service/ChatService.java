package com.skillexchange.service;

import com.skillexchange.dto.CreateChatMessageRequest;
import com.skillexchange.vo.ChatMessageVO;

import java.util.List;

public interface ChatService {

    ChatMessageVO send(Long userId, CreateChatMessageRequest request);

    List<ChatMessageVO> getConversation(Long userId, Long otherUserId);

    int getUnreadCount(Long userId);

    void markAsRead(Long userId, Long fromUserId);
}