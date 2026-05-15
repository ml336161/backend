package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.dto.CreateCommentRequest;
import com.skillexchange.service.CommentService;
import com.skillexchange.vo.CommentVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping
    public Result<CommentVO> create(@RequestAttribute Long userId, @Valid @RequestBody CreateCommentRequest request) {
        CommentVO comment = commentService.create(userId, request);
        return Result.success(comment);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return Result.success();
    }

    @GetMapping("/skill/{skillId}")
    public Result<List<CommentVO>> listBySkillId(@PathVariable Long skillId) {
        List<CommentVO> comments = commentService.listBySkillId(skillId);
        return Result.success(comments);
    }

    @GetMapping("/user/{userId}")
    public Result<List<CommentVO>> listByUserId(@PathVariable Long userId) {
        List<CommentVO> comments = commentService.listByUserId(userId);
        return Result.success(comments);
    }
}