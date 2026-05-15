package com.skillexchange.service;

import com.skillexchange.dto.CreateFriendApplyRequest;
import com.skillexchange.dto.HandleFriendApplyRequest;
import com.skillexchange.vo.FriendApplyVO;
import com.skillexchange.vo.FriendVO;

import java.util.List;

public interface FriendService {

    void apply(Long userId, CreateFriendApplyRequest request);

    void handle(Long userId, HandleFriendApplyRequest request);

    List<FriendApplyVO> listReceived(Long userId);

    List<FriendApplyVO> listSent(Long userId);

    List<FriendVO> listFriends(Long userId);

    void deleteFriend(Long userId, Long friendUserId);

    boolean isFriend(Long userId, Long otherUserId);
}