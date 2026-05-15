package com.skillexchange.service.impl;

import com.skillexchange.dto.CreateReportRequest;
import com.skillexchange.dto.HandleReportRequest;
import com.skillexchange.entity.Report;
import com.skillexchange.entity.User;
import com.skillexchange.exception.BusinessException;
import com.skillexchange.mapper.ReportMapper;
import com.skillexchange.mapper.UserMapper;
import com.skillexchange.service.ReportService;
import com.skillexchange.vo.ReportVO;
import com.skillexchange.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public void create(Long userId, CreateReportRequest request) {
        Report report = new Report();
        report.setReporterId(userId);
        report.setTargetType(request.getTargetType());
        report.setTargetId(request.getTargetId());
        report.setReason(request.getReason());
        report.setDescription(request.getDescription());
        report.setStatus("pending");
        reportMapper.insert(report);
    }

    @Override
    @Transactional
    public void handle(Long userId, HandleReportRequest request) {
        Report report = reportMapper.selectById(request.getId());
        if (report == null) {
            throw new BusinessException("举报不存在");
        }

        String status;
        switch (request.getStatus()) {
            case "approved":
                status = "approved";
                break;
            case "rejected":
                status = "rejected";
                break;
            default:
                throw new BusinessException("无效状态");
        }

        reportMapper.updateStatus(request.getId(), status, request.getHandleResult(), LocalDateTime.now(), userId);
    }

    @Override
    public List<ReportVO> listAll() {
        List<Report> reports = reportMapper.selectAll();
        return reports.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportVO> listPending() {
        List<Report> reports = reportMapper.selectByStatus("pending");
        return reports.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportVO> listByUserId(Long userId) {
        List<Report> reports = reportMapper.selectByReporterId(userId);
        return reports.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    private ReportVO convertToVO(Report report) {
        ReportVO vo = new ReportVO();
        vo.setId(report.getId());
        vo.setReporterId(report.getReporterId());
        vo.setTargetType(report.getTargetType());
        vo.setTargetId(report.getTargetId());
        vo.setReason(report.getReason());
        vo.setDescription(report.getDescription());
        vo.setStatus(report.getStatus());
        vo.setHandleResult(report.getHandleResult());
        vo.setHandleTime(report.getHandleTime());
        vo.setHandlerId(report.getHandlerId());
        vo.setCreateTime(report.getCreateTime());

        User reporter = userMapper.selectById(report.getReporterId());
        if (reporter != null) {
            UserVO reporterVO = new UserVO();
            reporterVO.setId(reporter.getId());
            reporterVO.setUsername(reporter.getUsername());
            reporterVO.setNickname(reporter.getNickname());
            reporterVO.setAvatar(reporter.getAvatar());
            vo.setReporter(reporterVO);
        }

        if (report.getHandlerId() != null) {
            User handler = userMapper.selectById(report.getHandlerId());
            if (handler != null) {
                UserVO handlerVO = new UserVO();
                handlerVO.setId(handler.getId());
                handlerVO.setUsername(handler.getUsername());
                handlerVO.setNickname(handler.getNickname());
                handlerVO.setAvatar(handler.getAvatar());
                vo.setHandler(handlerVO);
            }
        }

        return vo;
    }
}