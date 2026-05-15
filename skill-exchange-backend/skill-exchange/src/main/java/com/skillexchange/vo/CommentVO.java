package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentVO {

    private Long id;

    private Long skillId;

    private Long userId;

    private UserVO user;

    private Long exchangeId;

    private String content;

    private Integer rating;

    private Long parentId;

    private LocalDateTime createTime;
}