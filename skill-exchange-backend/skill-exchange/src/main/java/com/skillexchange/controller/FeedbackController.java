package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.dto.CreateFeedbackRequest;
import com.skillexchange.dto.ReplyFeedbackRequest;
import com.skillexchange.service.FeedbackService;
import com.skillexchange.vo.FeedbackVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    @PostMapping
    public Result<FeedbackVO> create(@RequestAttribute Long userId, @Valid @RequestBody CreateFeedbackRequest request) {
        FeedbackVO feedback = feedbackService.create(userId, request);
        return Result.success(feedback);
    }

    @GetMapping("/all")
    public Result<List<FeedbackVO>> listAll() {
        List<FeedbackVO> feedbacks = feedbackService.listAll();
        return Result.success(feedbacks);
    }

    @GetMapping("/my")
    public Result<List<FeedbackVO>> listMy(@RequestAttribute Long userId) {
        List<FeedbackVO> feedbacks = feedbackService.listByUserId(userId);
        return Result.success(feedbacks);
    }
}