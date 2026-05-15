package com.skillexchange.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkillVO {

    private Long id;

    private Long userId;

    private UserVO user;

    private Long typeId;

    private String typeName;

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

    private Boolean liked;

    private Boolean collected;

    private LocalDateTime createTime;
}