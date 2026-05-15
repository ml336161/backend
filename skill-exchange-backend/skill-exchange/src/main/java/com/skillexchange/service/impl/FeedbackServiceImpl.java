package com.skillexchange.service.impl;

import com.skillexchange.dto.CreateFeedbackRequest;
import com.skillexchange.dto.ReplyFeedbackRequest;
import com.skillexchange.entity.Feedback;
import com.skillexchange.entity.User;
import com.skillexchange.exception.BusinessException;
import com.skillexchange.mapper.FeedbackMapper;
import com.skillexchange.mapper.UserMapper;
import com.skillexchange.service.FeedbackService;
import com.skillexchange.vo.FeedbackVO;
import com.skillexchange.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Resource
    private FeedbackMapper feedbackMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public FeedbackVO create(Long userId, CreateFeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setType(request.getType());
        feedback.setTitle(request.getTitle());
        feedback.setContent(request.getContent());
        feedback.setImages(request.getImages());
        feedback.setContact(request.getContact());
        feedback.setStatus("pending");
        feedbackMapper.insert(feedback);

        User user = userMapper.selectById(userId);
        return convertToVO(feedback, user);
    }

    @Override
    @Transactional
    public FeedbackVO reply(Long userId, ReplyFeedbackRequest request) {
        Feedback feedback = feedbackMapper.selectById(request.getId());
        if (feedback == null) {
            throw new BusinessException("反馈不存在");
        }

        feedbackMapper.updateReply(request.getId(), request.getReply(), LocalDateTime.now());
        feedback = feedbackMapper.selectById(request.getId());
        User user = userMapper.selectById(feedback.getUserId());
        
        return convertToVO(feedback, user);
    }

    @Override
    public List<FeedbackVO> listAll() {
        return feedbackMapper.selectAll().stream()
                .map(feedback -> {
                    User user = userMapper.selectById(feedback.getUserId());
                    return convertToVO(feedback, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackVO> listByUserId(Long userId) {
        return feedbackMapper.selectByUserId(userId).stream()
                .map(feedback -> {
                    User user = userMapper.selectById(feedback.getUserId());
                    return convertToVO(feedback, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    public int countPending() {
        return feedbackMapper.countPending();
    }

    private FeedbackVO convertToVO(Feedback feedback, User user) {
        FeedbackVO vo = new FeedbackVO();
        vo.setId(feedback.getId());
        vo.setUserId(feedback.getUserId());
        vo.setType(feedback.getType());
        vo.setTitle(feedback.getTitle());
        vo.setContent(feedback.getContent());
        vo.setImages(feedback.getImages());
        vo.setContact(feedback.getContact());
        vo.setStatus(feedback.getStatus());
        vo.setReply(feedback.getReply());
        vo.setReplyTime(feedback.getReplyTime());
        vo.setCreateTime(feedback.getCreateTime());

        if (user != null) {
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setUsername(user.getUsername());
            userVO.setNickname(user.getNickname());
            userVO.setAvatar(user.getAvatar());
            vo.setUser(userVO);
        }
        return vo;
    }
}