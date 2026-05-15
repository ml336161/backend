package com.skillexchange.vo;

import lombok.Data;

@Data
public class DashboardStatsVO {

    private Long userCount;

    private Long skillCount;

    private Long exchangeCount;

    private Long pendingReportCount;

    private Long pendingExchangeCount;
}