package com.skillexchange.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String type;

    private String title;

    private String content;

    private String images;

    private String contact;

    private String status;

    private String reply;

    private LocalDateTime replyTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}