package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.service.CoinService;
import com.skillexchange.vo.CoinLogVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coin")
public class CoinController {

    @Resource
    private CoinService coinService;

    @PostMapping("/sign-in")
    public Result<Void> signIn(@RequestAttribute Long userId) {
        coinService.signIn(userId);
        return Result.success();
    }

    @GetMapping("/logs")
    public Result<List<CoinLogVO>> getLogs(@RequestAttribute Long userId) {
        List<CoinLogVO> logs = coinService.getLogs(userId);
        return Result.success(logs);
    }

    @GetMapping("/signed")
    public Result<Boolean> checkSigned(@RequestAttribute Long userId) {
        boolean signed = coinService.checkTodaySigned(userId);
        return Result.success(signed);
    }

    @GetMapping("/consecutive-days")
    public Result<Integer> getConsecutiveDays(@RequestAttribute Long userId) {
        int days = coinService.getConsecutiveDays(userId);
        return Result.success(days);
    }
}