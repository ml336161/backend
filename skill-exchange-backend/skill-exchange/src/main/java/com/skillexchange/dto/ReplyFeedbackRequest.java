package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReplyFeedbackRequest {

    @NotNull(message = "反馈ID不能为空")
    private Long id;

    @NotBlank(message = "回复内容不能为空")
    private String reply;
}