package com.skillexchange.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class CreateSkillExchangeRequest {

    @NotNull(message = "技能ID不能为空")
    private Long skillId;

    private String appointmentTime;  // 与前端字段名一致

    private String remark;
}