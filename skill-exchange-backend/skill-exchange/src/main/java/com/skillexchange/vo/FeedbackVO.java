package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackVO {

    private Long id;

    private Long userId;

    private UserVO user;

    private String type;

    private String title;

    private String content;

    private String images;

    private String contact;

    private String status;

    private String reply;

    private LocalDateTime replyTime;

    private LocalDateTime createTime;
}