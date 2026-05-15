package com.skillexchange.service;

import com.skillexchange.dto.CreateReportRequest;
import com.skillexchange.dto.HandleReportRequest;
import com.skillexchange.vo.ReportVO;

import java.util.List;

public interface ReportService {

    void create(Long userId, CreateReportRequest request);

    void handle(Long userId, HandleReportRequest request);

    List<ReportVO> listAll();

    List<ReportVO> listPending();

    List<ReportVO> listByUserId(Long userId);
}