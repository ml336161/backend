package com.skillexchange.service;

import com.skillexchange.vo.CoinLogVO;

import java.util.List;

public interface CoinService {

    void signIn(Long userId);

    List<CoinLogVO> getLogs(Long userId);

    boolean checkTodaySigned(Long userId);

    int getConsecutiveDays(Long userId);
}