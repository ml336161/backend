package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateChatMessageRequest {

    @NotNull(message = "接收者ID不能为空")
    private Long toUserId;

    private Long exchangeId;

    @NotBlank(message = "消息内容不能为空")
    private String content;

    private String type = "text";
}