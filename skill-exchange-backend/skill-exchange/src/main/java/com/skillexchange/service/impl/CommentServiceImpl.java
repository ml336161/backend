package com.skillexchange.service.impl;

import com.skillexchange.dto.CreateCommentRequest;
import com.skillexchange.entity.Comment;
import com.skillexchange.entity.User;
import com.skillexchange.exception.BusinessException;
import com.skillexchange.mapper.CommentMapper;
import com.skillexchange.mapper.UserMapper;
import com.skillexchange.service.CommentService;
import com.skillexchange.vo.CommentVO;
import com.skillexchange.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public CommentVO create(Long userId, CreateCommentRequest request) {
        Comment comment = new Comment();
        comment.setSkillId(request.getSkillId());
        comment.setUserId(userId);
        comment.setContent(request.getContent());
        comment.setRating(request.getRating());
        comment.setParentId(request.getParentId());
        commentMapper.insert(comment);

        User user = userMapper.selectById(userId);
        return convertToVO(comment, user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        commentMapper.deleteById(id);
    }

    @Override
    public List<CommentVO> listBySkillId(Long skillId) {
        List<Comment> comments = commentMapper.selectBySkillId(skillId);
        return comments.stream()
                .map(comment -> {
                    User user = userMapper.selectById(comment.getUserId());
                    return convertToVO(comment, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentVO> listByUserId(Long userId) {
        List<Comment> comments = commentMapper.selectByUserId(userId);
        User user = userMapper.selectById(userId);
        return comments.stream()
                .map(comment -> convertToVO(comment, user))
                .collect(Collectors.toList());
    }

    private CommentVO convertToVO(Comment comment, User user) {
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setSkillId(comment.getSkillId());
        vo.setUserId(comment.getUserId());
        vo.setExchangeId(comment.getExchangeId());
        vo.setContent(comment.getContent());
        vo.setRating(comment.getRating());
        vo.setParentId(comment.getParentId());
        vo.setCreateTime(comment.getCreateTime());

        if (user != null) {
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setUsername(user.getUsername());
            userVO.setNickname(user.getNickname());
            userVO.setAvatar(user.getAvatar());
            vo.setUser(userVO);
        }
        return vo;
    }
}