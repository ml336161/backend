package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkillExchangeVO {

    private Long id;

    private Long skillId;

    private SkillVO skill;

    private Long providerId;

    private UserVO provider;

    private Long requesterId;

    private UserVO requester;

    private String status;

    private Integer price;

    private LocalDateTime scheduledTime;

    private LocalDateTime actualTime;

    private String remark;

    private LocalDateTime createTime;
}