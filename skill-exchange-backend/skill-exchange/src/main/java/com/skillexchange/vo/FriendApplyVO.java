package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendApplyVO {

    private Long id;

    private Long fromUserId;

    private UserVO fromUser;

    private Long toUserId;

    private UserVO toUser;

    private String status;

    private String message;

    private LocalDateTime createTime;
}