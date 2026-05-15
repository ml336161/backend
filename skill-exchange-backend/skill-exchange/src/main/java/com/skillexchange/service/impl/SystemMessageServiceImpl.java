package com.skillexchange.service.impl;

import com.skillexchange.entity.SystemMessage;
import com.skillexchange.mapper.SystemMessageMapper;
import com.skillexchange.service.SystemMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemMessageServiceImpl implements SystemMessageService {

    @Resource
    private SystemMessageMapper systemMessageMapper;

    public static final int TYPE_WELCOME = 1;
    public static final int TYPE_FEEDBACK_REPLY = 2;
    public static final int TYPE_SYSTEM_NOTICE = 3;

    public static final int STATUS_UNREAD = 0;
    public static final int STATUS_READ = 1;

    @Override
    public void sendMessage(Long userId, String content, Integer type) {
        SystemMessage message = new SystemMessage();
        message.setUserId(userId);
        message.setContent(content);
        message.setType(type);
        message.setStatus(STATUS_UNREAD);
        systemMessageMapper.insert(message);
    }

    @Override
    public void sendWelcomeMessage(Long userId) {
        String content = "欢迎加入技能互助平台！在这里你可以学习新技能，也可以分享自己的技能，与其他用户进行技能交换。祝你在平台上度过愉快的时光！";
        sendMessage(userId, content, TYPE_WELCOME);
    }

    @Override
    public void sendFeedbackReply(Long userId, String content) {
        sendMessage(userId, content, TYPE_FEEDBACK_REPLY);
    }

    @Override
    public List<SystemMessage> getMessages(Long userId) {
        return systemMessageMapper.selectByUserId(userId);
    }

    @Override
    public int getUnreadCount(Long userId) {
        return systemMessageMapper.countUnread(userId);
    }

    @Override
    public void markAllRead(Long userId) {
        systemMessageMapper.updateAllStatusByUserId(userId, STATUS_READ);
    }
}
