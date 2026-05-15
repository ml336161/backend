package com.skillexchange.mapper;

import com.skillexchange.entity.SkillLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkillLikeMapper {

    int insert(SkillLike skillLike);

    int deleteByUserIdAndSkillId(@Param("userId") Long userId, @Param("skillId") Long skillId);

    SkillLike selectByUserIdAndSkillId(@Param("userId") Long userId, @Param("skillId") Long skillId);

    List<SkillLike> selectByUserId(Long userId);

    int countBySkillId(Long skillId);

    int countByUserId(Long userId);
}