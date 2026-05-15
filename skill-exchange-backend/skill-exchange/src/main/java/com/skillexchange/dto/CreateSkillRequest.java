package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateSkillRequest {

    @NotNull(message = "技能类型不能为空")
    private Long typeId;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    private String images;

    private Integer price = 1;

    private String duration;

    private String location;
}