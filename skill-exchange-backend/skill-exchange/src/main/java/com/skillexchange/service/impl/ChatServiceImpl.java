package com.skillexchange.service.impl;

import com.skillexchange.dto.CreateChatMessageRequest;
import com.skillexchange.entity.ChatMessage;
import com.skillexchange.entity.User;
import com.skillexchange.exception.BusinessException;
import com.skillexchange.mapper.ChatMessageMapper;
import com.skillexchange.mapper.UserMapper;
import com.skillexchange.service.ChatService;
import com.skillexchange.service.FriendService;
import com.skillexchange.vo.ChatMessageVO;
import com.skillexchange.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private ChatMessageMapper chatMessageMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private FriendService friendService;

    @Override
    @Transactional
    public ChatMessageVO send(Long userId, CreateChatMessageRequest request) {
        if (userId.equals(request.getToUserId())) {
            throw new BusinessException("不能给自己发消息");
        }

        User toUser = userMapper.selectById(request.getToUserId());
        if (toUser == null) {
            throw new BusinessException("用户不存在");
        }

        ChatMessage message = new ChatMessage();
        message.setFromUserId(userId);
        message.setToUserId(request.getToUserId());
        message.setExchangeId(request.getExchangeId());
        message.setType(request.getType() != null ? request.getType() : "text");
        message.setContent(request.getContent());
        message.setIsRead(0);
        chatMessageMapper.insert(message);

        User user = userMapper.selectById(userId);
        return convertToVO(message, user, toUser);
    }

    @Override
    public List<ChatMessageVO> getConversation(Long userId, Long otherUserId) {
        List<ChatMessage> messages = chatMessageMapper.selectConversation(userId, otherUserId);
        User fromUser = userMapper.selectById(userId);
        User toUser = userMapper.selectById(otherUserId);
        return messages.stream()
                .map(msg -> convertToVO(msg, fromUser, toUser))
                .collect(Collectors.toList());
    }

    @Override
    public int getUnreadCount(Long userId) {
        return chatMessageMapper.countUnreadByUserId(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long userId, Long fromUserId) {
        chatMessageMapper.updateReadStatus(fromUserId, userId);
    }

    private ChatMessageVO convertToVO(ChatMessage message, User fromUser, User toUser) {
        ChatMessageVO vo = new ChatMessageVO();
        vo.setId(message.getId());
        vo.setFromUserId(message.getFromUserId());
        vo.setToUserId(message.getToUserId());
        vo.setExchangeId(message.getExchangeId());
        vo.setType(message.getType());
        vo.setContent(message.getContent());
        vo.setIsRead(message.getIsRead());
        vo.setCreateTime(message.getCreateTime());

        if (fromUser != null) {
            UserVO userVO = new UserVO();
            userVO.setId(fromUser.getId());
            userVO.setUsername(fromUser.getUsername());
            userVO.setNickname(fromUser.getNickname());
            userVO.setAvatar(fromUser.getAvatar());
            vo.setFromUser(userVO);
        }
        if (toUser != null) {
            UserVO userVO = new UserVO();
            userVO.setId(toUser.getId());
            userVO.setUsername(toUser.getUsername());
            userVO.setNickname(toUser.getNickname());
            userVO.setAvatar(toUser.getAvatar());
            vo.setToUser(userVO);
        }
        return vo;
    }
}