package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private String email;

    private String phone;

    private LocalDate birthday;

    private String role;

    private Integer timeCoin;

    private Integer creditScore;

    private Integer status;

    private LocalDateTime createTime;
}