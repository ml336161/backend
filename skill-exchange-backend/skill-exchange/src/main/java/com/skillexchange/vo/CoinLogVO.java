package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CoinLogVO {

    private Long id;

    private Long userId;

    private String type;

    private Integer amount;

    private Integer balance;

    private String relatedType;

    private Long relatedId;

    private String description;

    private LocalDateTime createTime;
}