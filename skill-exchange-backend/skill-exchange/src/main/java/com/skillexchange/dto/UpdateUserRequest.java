package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UpdateUserRequest {

    private String nickname;

    private String avatar;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;
}