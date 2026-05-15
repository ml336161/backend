package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UpdateUserRequest {

    private String nickname;

    private String avatar;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;

    private LocalDate birthday;

    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;
}