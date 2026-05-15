package com.skillexchange.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Skill implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long typeId;

    private String title;

    private String description;

    private String images;

    private Integer price;

    private String duration;

    private String location;

    private Integer viewCount;

    private Integer likeCount;

    private Integer collectCount;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}