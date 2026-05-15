package com.skillexchange.mapper;

import com.skillexchange.entity.SkillCollect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkillCollectMapper {

    int insert(SkillCollect skillCollect);

    int deleteByUserIdAndSkillId(@Param("userId") Long userId, @Param("skillId") Long skillId);

    SkillCollect selectByUserIdAndSkillId(@Param("userId") Long userId, @Param("skillId") Long skillId);

    List<SkillCollect> selectByUserId(Long userId);

    int countBySkillId(Long skillId);

    int countByUserId(Long userId);
}