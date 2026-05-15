package com.skillexchange.controller;

import com.skillexchange.common.Result;
import com.skillexchange.dto.CreateFriendApplyRequest;
import com.skillexchange.dto.HandleFriendApplyRequest;
import com.skillexchange.service.FriendService;
import com.skillexchange.vo.FriendApplyVO;
import com.skillexchange.vo.FriendVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Resource
    private FriendService friendService;

    @PostMapping("/apply")
    public Result<Void> apply(@RequestAttribute Long userId, @Valid @RequestBody CreateFriendApplyRequest request) {
        friendService.apply(userId, request);
        return Result.success();
    }

    @PutMapping("/handle")
    public Result<Void> handle(@RequestAttribute Long userId, @Valid @RequestBody HandleFriendApplyRequest request) {
        friendService.handle(userId, request);
        return Result.success();
    }

    @GetMapping("/received")
    public Result<List<FriendApplyVO>> listReceived(@RequestAttribute Long userId) {
        List<FriendApplyVO> applies = friendService.listReceived(userId);
        return Result.success(applies);
    }

    @GetMapping("/sent")
    public Result<List<FriendApplyVO>> listSent(@RequestAttribute Long userId) {
        List<FriendApplyVO> applies = friendService.listSent(userId);
        return Result.success(applies);
    }

    @GetMapping
    public Result<List<FriendVO>> listFriends(@RequestAttribute Long userId) {
        List<FriendVO> friends = friendService.listFriends(userId);
        return Result.success(friends);
    }

    @DeleteMapping("/{friendUserId}")
    public Result<Void> deleteFriend(@RequestAttribute Long userId, @PathVariable Long friendUserId) {
        friendService.deleteFriend(userId, friendUserId);
        return Result.success();
    }

    @GetMapping("/check/{otherUserId}")
    public Result<Boolean> checkFriend(@RequestAttribute Long userId, @PathVariable Long otherUserId) {
        boolean isFriend = friendService.isFriend(userId, otherUserId);
        return Result.success(isFriend);
    }
}