package com.skillexchange.service;

import com.skillexchange.dto.CreateSkillExchangeRequest;
import com.skillexchange.dto.HandleSkillExchangeRequest;
import com.skillexchange.vo.SkillExchangeVO;

import java.util.List;

public interface SkillExchangeService {

    SkillExchangeVO create(Long userId, CreateSkillExchangeRequest request);

    void handle(Long userId, HandleSkillExchangeRequest request);

    SkillExchangeVO getById(Long id);

    List<SkillExchangeVO> listReceived(Long userId);

    List<SkillExchangeVO> listSent(Long userId);

    Long countPending(Long userId);
}