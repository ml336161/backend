package com.skillexchange.vo;

import lombok.Data;

import java.util.List;

@Data
public class SkillListResponse {

    private List<SkillVO> list;

    private Long total;

    private Integer pageNum;

    private Integer pageSize;
}