package com.skillexchange.service;

import com.skillexchange.dto.CreateFeedbackRequest;
import com.skillexchange.dto.ReplyFeedbackRequest;
import com.skillexchange.vo.FeedbackVO;

import java.util.List;

public interface FeedbackService {

    FeedbackVO create(Long userId, CreateFeedbackRequest request);

    FeedbackVO reply(Long userId, ReplyFeedbackRequest request);

    List<FeedbackVO> listAll();

    List<FeedbackVO> listByUserId(Long userId);

    int countPending();
}