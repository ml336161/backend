package com.skillexchange.service;

import com.skillexchange.dto.CreateSkillRequest;
import com.skillexchange.dto.UpdateSkillRequest;
import com.skillexchange.entity.Skill;
import com.skillexchange.vo.SkillListResponse;
import com.skillexchange.vo.SkillVO;

import java.util.List;

public interface SkillService {

    SkillVO create(Long userId, CreateSkillRequest request);

    SkillVO update(Long userId, UpdateSkillRequest request);

    void delete(Long id);

    SkillVO getById(Long id);

    SkillVO getByIdWithUser(Long id);

    SkillListResponse list(Integer pageNum, Integer pageSize, Long typeId, String keyword, Integer minPrice, Integer maxPrice);

    List<SkillVO> listByUserId(Long userId);

    void like(Long userId, Long skillId);

    void collect(Long userId, Long skillId);

    void updateViewCount(Long id);

    Skill getEntityById(Long id);

    Long count();

    List<SkillVO> listHotSkills(Integer limit);

    List<SkillVO> listLatestSkills(Integer limit);

    List<SkillVO> listUserCollects(Long userId);

    List<SkillVO> listUserLikes(Long userId);

    int countUserCollects(Long userId);

    int countUserLikes(Long userId);

    List<SkillVO> listUserCollectsByUserId(Long userId);

    List<SkillVO> listUserLikesByUserId(Long userId);

    List<SkillVO> listAll();
}