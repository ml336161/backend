package com.skillexchange.mapper;

import com.skillexchange.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    int insert(Comment comment);

    int deleteById(Long id);

    Comment selectById(Long id);

    List<Comment> selectBySkillId(Long skillId);

    List<Comment> selectByUserId(Long userId);

    int countBySkillId(Long skillId);

    int countByUserId(Long userId);
}