package com.skillexchange.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long friendUserId;

    private String remark;

    private LocalDateTime createTime;
}