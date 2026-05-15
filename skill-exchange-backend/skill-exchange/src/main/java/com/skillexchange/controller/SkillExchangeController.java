package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.dto.CreateSkillExchangeRequest;
import com.skillexchange.dto.HandleSkillExchangeRequest;
import com.skillexchange.service.SkillExchangeService;
import com.skillexchange.vo.SkillExchangeVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/exchanges")
public class SkillExchangeController {

    @Resource
    private SkillExchangeService skillExchangeService;

    @PostMapping
    public Result<SkillExchangeVO> create(@RequestAttribute Long userId, @Valid @RequestBody CreateSkillExchangeRequest request) {
        SkillExchangeVO exchange = skillExchangeService.create(userId, request);
        return Result.success(exchange);
    }

    @PutMapping("/handle")
    public Result<Void> handle(@RequestAttribute Long userId, @Valid @RequestBody HandleSkillExchangeRequest request) {
        skillExchangeService.handle(userId, request);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SkillExchangeVO> getById(@PathVariable Long id) {
        SkillExchangeVO exchange = skillExchangeService.getById(id);
        return Result.success(exchange);
    }

    @GetMapping("/received")
    public Result<List<SkillExchangeVO>> listReceived(@RequestAttribute Long userId) {
        List<SkillExchangeVO> exchanges = skillExchangeService.listReceived(userId);
        return Result.success(exchanges);
    }

    @GetMapping("/sent")
    public Result<List<SkillExchangeVO>> listSent(@RequestAttribute Long userId) {
        List<SkillExchangeVO> exchanges = skillExchangeService.listSent(userId);
        return Result.success(exchanges);
    }

    @GetMapping("/pending-count")
    public Result<Long> getPendingCount(@RequestAttribute Long userId) {
        Long count = skillExchangeService.countPending(userId);
        return Result.success(count);
    }
}