package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageVO {

    private Long id;

    private Long fromUserId;

    private UserVO fromUser;

    private Long toUserId;

    private UserVO toUser;

    private Long exchangeId;

    private String type;

    private String content;

    private Integer isRead;

    private LocalDateTime createTime;
}