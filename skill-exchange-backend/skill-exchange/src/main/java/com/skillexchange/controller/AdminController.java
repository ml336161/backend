package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.dto.HandleReportRequest;
import com.skillexchange.dto.ReplyFeedbackRequest;
import com.skillexchange.mapper.*;
import com.skillexchange.service.*;
import com.skillexchange.vo.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private UserService userService;

    @Resource
    private SkillService skillService;

    @Resource
    private ReportService reportService;

    @Resource
    private FeedbackService feedbackService;

    @Resource
    private SkillExchangeService skillExchangeService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private SkillMapper skillMapper;

    @Resource
    private ReportMapper reportMapper;

    @Resource
    private SkillExchangeMapper skillExchangeMapper;

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userMapper.countAll());
        stats.put("skillCount", skillService.count());
        stats.put("exchangeCount", skillExchangeMapper.countAll());
        stats.put("pendingReportCount", reportMapper.countByStatus("pending"));
        stats.put("pendingExchangeCount", skillExchangeMapper.countByStatus("pending"));
        stats.put("pendingFeedbackCount", feedbackService.countPending());
        return Result.success(stats);
    }

    @PutMapping("/user/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.success();
    }

    @DeleteMapping("/skill/{id}")
    public Result<Void> deleteSkill(@PathVariable Long id) {
        skillService.delete(id);
        return Result.success();
    }

    @PutMapping("/report/handle")
    public Result<Void> handleReport(@RequestAttribute Long userId, @Valid @RequestBody HandleReportRequest request) {
        reportService.handle(userId, request);
        return Result.success();
    }

    @PutMapping("/feedback/reply")
    public Result<Void> replyFeedback(@RequestAttribute Long userId, @Valid @RequestBody ReplyFeedbackRequest request) {
        feedbackService.reply(userId, request);
        return Result.success();
    }

    @GetMapping("/reports")
    public Result<List<ReportVO>> getReports(@RequestParam(required = false) String status) {
        List<ReportVO> reports;
        if (status != null && "pending".equals(status)) {
            reports = reportService.listPending();
        } else {
            reports = reportService.listAll();
        }
        return Result.success(reports);
    }

    @GetMapping("/feedbacks")
    public Result<List<FeedbackVO>> getFeedbacks() {
        List<FeedbackVO> feedbacks = feedbackService.listAll();
        return Result.success(feedbacks);
    }

    @GetMapping("/users")
    public Result<List<UserVO>> getUsers() {
        List<UserVO> users = userService.listAll();
        return Result.success(users);
    }

    @GetMapping("/skills")
    public Result<List<SkillVO>> getSkills() {
        List<SkillVO> skills = skillService.listAll();
        return Result.success(skills);
    }

    @GetMapping("/exchanges")
    public Result<List<SkillExchangeVO>> getExchanges() {
        List<SkillExchangeVO> exchanges = skillExchangeService.listAll();
        return Result.success(exchanges);
    }
}