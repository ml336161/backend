package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.service.SkillTypeService;
import com.skillexchange.vo.SkillTypeVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/skill-types")
public class SkillTypeController {

    @Resource
    private SkillTypeService skillTypeService;

    @GetMapping("/{id}")
    public Result<SkillTypeVO> getById(@PathVariable Long id) {
        SkillTypeVO type = skillTypeService.getById(id);
        return Result.success(type);
    }

    @GetMapping
    public Result<List<SkillTypeVO>> listAll() {
        List<SkillTypeVO> types = skillTypeService.listAll();
        return Result.success(types);
    }

    @GetMapping("/active")
    public Result<List<SkillTypeVO>> listActive() {
        List<SkillTypeVO> types = skillTypeService.listActive();
        return Result.success(types);
    }
}