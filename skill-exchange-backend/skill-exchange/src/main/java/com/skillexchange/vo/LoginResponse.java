package com.skillexchange.vo;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;

    private UserVO user;
}