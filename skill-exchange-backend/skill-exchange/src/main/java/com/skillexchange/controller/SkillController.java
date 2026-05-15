package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.dto.CreateSkillRequest;
import com.skillexchange.dto.UpdateSkillRequest;
import com.skillexchange.service.SkillService;
import com.skillexchange.vo.SkillListResponse;
import com.skillexchange.vo.SkillVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Resource
    private SkillService skillService;

    @PostMapping
    public Result<SkillVO> create(@RequestAttribute Long userId, @Valid @RequestBody CreateSkillRequest request) {
        SkillVO skill = skillService.create(userId, request);
        return Result.success(skill);
    }

    @PutMapping
    public Result<SkillVO> update(@RequestAttribute Long userId, @Valid @RequestBody UpdateSkillRequest request) {
        SkillVO skill = skillService.update(userId, request);
        return Result.success(skill);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        skillService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SkillVO> getById(@PathVariable Long id) {
        SkillVO skill = skillService.getByIdWithUser(id);
        return Result.success(skill);
    }

    @GetMapping
    public Result<SkillListResponse> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice) {
        SkillListResponse response = skillService.list(pageNum, pageSize, typeId, keyword, minPrice, maxPrice);
        return Result.success(response);
    }

    @GetMapping("/user/{userId}")
    public Result<List<SkillVO>> listByUserId(@PathVariable Long userId) {
        List<SkillVO> skills = skillService.listByUserId(userId);
        return Result.success(skills);
    }

    @GetMapping("/hot")
    public Result<List<SkillVO>> listHotSkills(@RequestParam(defaultValue = "6") Integer limit) {
        List<SkillVO> skills = skillService.listHotSkills(limit);
        return Result.success(skills);
    }

    @GetMapping("/latest")
    public Result<List<SkillVO>> listLatestSkills(@RequestParam(defaultValue = "6") Integer limit) {
        List<SkillVO> skills = skillService.listLatestSkills(limit);
        return Result.success(skills);
    }

    @GetMapping("/collects")
    public Result<List<SkillVO>> listUserCollects(@RequestAttribute Long userId) {
        List<SkillVO> skills = skillService.listUserCollects(userId);
        return Result.success(skills);
    }

    @GetMapping("/likes")
    public Result<List<SkillVO>> listUserLikes(@RequestAttribute Long userId) {
        List<SkillVO> skills = skillService.listUserLikes(userId);
        return Result.success(skills);
    }

    @GetMapping("/collects/count")
    public Result<Integer> countUserCollects(@RequestAttribute Long userId) {
        int count = skillService.countUserCollects(userId);
        return Result.success(count);
    }

    @GetMapping("/likes/count")
    public Result<Integer> countUserLikes(@RequestAttribute Long userId) {
        int count = skillService.countUserLikes(userId);
        return Result.success(count);
    }

    @GetMapping("/user/{userId}/likes")
    public Result<List<SkillVO>> listUserLikesByUserId(@PathVariable Long userId) {
        List<SkillVO> skills = skillService.listUserLikesByUserId(userId);
        return Result.success(skills);
    }

    @GetMapping("/user/{userId}/collects")
    public Result<List<SkillVO>> listUserCollectsByUserId(@PathVariable Long userId) {
        List<SkillVO> skills = skillService.listUserCollectsByUserId(userId);
        return Result.success(skills);
    }

    @PostMapping("/{id}/like")
    public Result<Void> like(@RequestAttribute Long userId, @PathVariable Long id) {
        skillService.like(userId, id);
        return Result.success();
    }

    @PostMapping("/{id}/collect")
    public Result<Void> collect(@RequestAttribute Long userId, @PathVariable Long id) {
        skillService.collect(userId, id);
        return Result.success();
    }
}