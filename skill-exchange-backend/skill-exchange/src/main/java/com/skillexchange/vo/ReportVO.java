package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportVO {

    private Long id;

    private Long reporterId;

    private UserVO reporter;

    private String targetType;

    private Long targetId;

    private String reason;

    private String description;

    private String status;

    private String handleResult;

    private LocalDateTime handleTime;

    private Long handlerId;

    private UserVO handler;

    private LocalDateTime createTime;
}