package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateSkillRequest {

    @NotNull(message = "技能ID不能为空")
    private Long id;

    private Long typeId;

    private String title;

    private String description;

    private String images;

    private Integer price;

    private String duration;

    private String location;
}