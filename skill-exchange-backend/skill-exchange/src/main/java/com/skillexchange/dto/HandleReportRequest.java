package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class HandleReportRequest {

    @NotNull(message = "举报ID不能为空")
    private Long id;

    @NotBlank(message = "处理结果不能为空")
    private String status;

    private String handleResult;
}