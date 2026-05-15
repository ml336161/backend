package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.dto.CreateReportRequest;
import com.skillexchange.dto.HandleReportRequest;
import com.skillexchange.service.ReportService;
import com.skillexchange.vo.ReportVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Resource
    private ReportService reportService;

    @PostMapping
    public Result<Void> create(@RequestAttribute Long userId, @Valid @RequestBody CreateReportRequest request) {
        reportService.create(userId, request);
        return Result.success();
    }

    @GetMapping("/all")
    public Result<List<ReportVO>> listAll() {
        List<ReportVO> reports = reportService.listAll();
        return Result.success(reports);
    }

    @GetMapping("/pending")
    public Result<List<ReportVO>> listPending() {
        List<ReportVO> reports = reportService.listPending();
        return Result.success(reports);
    }

    @GetMapping("/my")
    public Result<List<ReportVO>> listMyReports(@RequestAttribute Long userId) {
        List<ReportVO> reports = reportService.listByUserId(userId);
        return Result.success(reports);
    }
}