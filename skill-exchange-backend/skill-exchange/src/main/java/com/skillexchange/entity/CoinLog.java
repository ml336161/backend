package com.skillexchange.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CoinLog implements Serializable {

    private static final long serialVersionUID = 1L;

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