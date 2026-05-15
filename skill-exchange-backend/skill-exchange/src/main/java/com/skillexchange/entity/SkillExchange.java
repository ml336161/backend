package com.skillexchange.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SkillExchange implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long skillId;

    private Long providerId;

    private Long requesterId;

    private String status;

    private Integer price;

    private LocalDateTime scheduledTime;

    private LocalDateTime actualTime;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}