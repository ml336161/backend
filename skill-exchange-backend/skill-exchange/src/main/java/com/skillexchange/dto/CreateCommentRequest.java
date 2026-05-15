package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateCommentRequest {

    @NotNull(message = "技能ID不能为空")
    private Long skillId;

    @NotBlank(message = "评论内容不能为空")
    private String content;

    private Integer rating;

    private Long parentId;
}