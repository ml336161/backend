package com.skillexchange.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
public class UpdateUserRequest {

    private String nickname;

    private String avatar;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;

    private LocalDate birthday;
}