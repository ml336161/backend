package com.skillexchange.mapper;

import com.skillexchange.entity.SkillType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SkillTypeMapper {

    int insert(SkillType skillType);

    int update(SkillType skillType);

    int deleteById(Long id);

    SkillType selectById(Long id);

    SkillType selectByName(String name);

    List<SkillType> selectAll();

    List<SkillType> selectActive();

    int countAll();
}