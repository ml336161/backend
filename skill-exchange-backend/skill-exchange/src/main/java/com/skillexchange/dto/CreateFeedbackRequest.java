package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateFeedbackRequest {

    @NotBlank(message = "反馈类型不能为空")
    private String type;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    private String images;

    private String contact;
}