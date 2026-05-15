package com.skillexchange.service;

import com.skillexchange.entity.SkillType;
import com.skillexchange.vo.SkillTypeVO;

import java.util.List;

public interface SkillTypeService {

    SkillTypeVO getById(Long id);

    List<SkillTypeVO> listAll();

    List<SkillTypeVO> listActive();

    SkillType getEntityById(Long id);
}