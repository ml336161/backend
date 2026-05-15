package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendVO {

    private Long id;

    private Long userId;

    private Long friendUserId;

    private UserVO friendUser;

    private String remark;

    private Integer unreadCount;

    private LocalDateTime createTime;
}