package com.skillexchange.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long skillId;

    private Long userId;

    private Long exchangeId;

    private String content;

    private Integer rating;

    private Long parentId;

    private LocalDateTime createTime;
}