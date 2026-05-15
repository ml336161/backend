package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class HandleFriendApplyRequest {

    @NotNull(message = "申请ID不能为空")
    private Long id;

    @NotBlank(message = "操作类型不能为空")
    private String action;
}