package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkillTypeVO {

    private Long id;

    private String name;

    private String description;

    private String icon;

    private Integer sort;

    private Integer status;

    private LocalDateTime createTime;
}