package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateFriendApplyRequest {

    @NotNull(message = "好友用户ID不能为空")
    private Long toUserId;

    private String message;
}