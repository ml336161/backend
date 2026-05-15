package com.skillexchange.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysNotification implements Serializable {

    private static final long serialVersionUID = 1L;

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