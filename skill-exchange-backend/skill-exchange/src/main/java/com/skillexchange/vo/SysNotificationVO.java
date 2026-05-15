package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysNotificationVO {

    private Long id;

    private Long userId;

    private String type;

    private String title;

    private String content;

    private String relatedType;

    private Long relatedId;

    private Integer isRead;

    private LocalDateTime createTime;
}