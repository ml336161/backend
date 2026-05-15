package com.skillexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemMessage {

    private Long id;

    private Long userId;

    private String content;

    private Integer type;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
