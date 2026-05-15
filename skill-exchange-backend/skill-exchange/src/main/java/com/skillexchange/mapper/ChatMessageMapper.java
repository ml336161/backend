package com.skillexchange.mapper;

import com.skillexchange.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageMapper {

    int insert(ChatMessage chatMessage);

    List<ChatMessage> selectConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    List<ChatMessage> selectUnreadByUserId(Long userId);

    int countUnreadByUserId(Long userId);

    int countUnreadByFromUserIdAndToUserId(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    int updateReadStatus(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);
}