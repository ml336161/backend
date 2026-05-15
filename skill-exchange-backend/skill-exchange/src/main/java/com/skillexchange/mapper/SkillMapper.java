package com.skillexchange.mapper;

import com.skillexchange.entity.Skill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkillMapper {

    int insert(Skill skill);

    int update(Skill skill);

    int deleteById(Long id);

    Skill selectById(Long id);

    List<Skill> selectAll();

    List<Skill> selectByUserId(Long userId);

    List<Skill> selectByTypeId(Long typeId);

    List<Skill> selectByKeyword(@Param("keyword") String keyword);

    List<Skill> selectPage(@Param("typeId") Long typeId, @Param("keyword") String keyword,
                           @Param("offset") Integer offset, @Param("limit") Integer limit);

    int countPage(@Param("typeId") Long typeId, @Param("keyword") String keyword);

    int countByUserId(Long userId);

    int countAll();

    int updateViewCount(Long id);

    int updateLikeCount(@Param("id") Long id, @Param("count") Integer count);

    int updateCollectCount(@Param("id") Long id, @Param("count") Integer count);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}