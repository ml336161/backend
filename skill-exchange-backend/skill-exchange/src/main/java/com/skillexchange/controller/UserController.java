package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.dto.LoginRequest;
import com.skillexchange.dto.RegisterRequest;
import com.skillexchange.dto.UpdatePasswordRequest;
import com.skillexchange.dto.UpdateUserRequest;
import com.skillexchange.service.UserService;
import com.skillexchange.vo.CreditRadarVO;
import com.skillexchange.vo.LoginResponse;
import com.skillexchange.vo.ProfileStatsVO;
import com.skillexchange.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;





    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return Result.success(response);
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable Long id) {
        UserVO user = userService.getById(id);
        return Result.success(user);
    }

    @GetMapping("/current")
    public Result<UserVO> getCurrentUser(@RequestAttribute Long userId) {
        UserVO user = userService.getById(userId);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result<UserVO> update(@RequestAttribute Long userId, @RequestBody UpdateUserRequest request) {
        UserVO user = userService.update(userId, request);
        return Result.success(user);
    }

    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestAttribute Long userId, @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(userId, request);
        return Result.success();
    }

    @GetMapping("/credit-radar")
    public Result<CreditRadarVO> getCreditRadar(@RequestAttribute Long userId) {
        CreditRadarVO radar = userService.getCreditRadar(userId);
        return Result.success(radar);
    }

    @GetMapping("/profile-stats")
    public Result<ProfileStatsVO> getProfileStats(@RequestAttribute Long userId) {
        ProfileStatsVO stats = userService.getProfileStats(userId);
        return Result.success(stats);
    }

    @GetMapping("/list")
    public Result<List<UserVO>> listAll() {
        List<UserVO> users = userService.listAll();
        return Result.success(users);
    }

    @GetMapping("/list/enabled")
    public Result<List<UserVO>> listEnabled() {
        List<UserVO> users = userService.listByStatus(1);
        return Result.success(users);
    }
}