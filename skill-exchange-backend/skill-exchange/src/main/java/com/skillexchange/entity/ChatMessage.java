package com.skillexchange.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long fromUserId;

    private Long toUserId;

    private Long exchangeId;

    private String type;

    private String content;

    private Integer isRead;

    private LocalDateTime createTime;
}