package com.skillexchange.vo;

import lombok.Data;

@Data
public class ProfileStatsVO {

    private Integer collectCount;

    private Integer likeCount;

    private Integer friendCount;

    private Integer skillCount;

    private Integer appliedExchangeCount;

    private Integer receivedExchangeCount;
}