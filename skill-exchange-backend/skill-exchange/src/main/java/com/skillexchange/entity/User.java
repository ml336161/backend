package com.skillexchange.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String password;

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

    private LocalDateTime updateTime;
}