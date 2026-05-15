package com.skillexchange.service;

import com.skillexchange.dto.CreateCommentRequest;
import com.skillexchange.vo.CommentVO;

import java.util.List;

public interface CommentService {

    CommentVO create(Long userId, CreateCommentRequest request);

    void delete(Long id);

    List<CommentVO> listBySkillId(Long skillId);

    List<CommentVO> listByUserId(Long userId);
}