package com.skillexchange.service.impl;

import com.skillexchange.dto.CreateFriendApplyRequest;
import com.skillexchange.dto.HandleFriendApplyRequest;
import com.skillexchange.entity.Friend;
import com.skillexchange.entity.FriendApply;
import com.skillexchange.entity.User;
import com.skillexchange.exception.BusinessException;
import com.skillexchange.mapper.FriendApplyMapper;
import com.skillexchange.mapper.FriendMapper;
import com.skillexchange.mapper.UserMapper;
import com.skillexchange.service.FriendService;
import com.skillexchange.vo.FriendApplyVO;
import com.skillexchange.vo.FriendVO;
import com.skillexchange.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendServiceImpl implements FriendService {

    @Resource
    private FriendApplyMapper friendApplyMapper;

    @Resource
    private FriendMapper friendMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public void apply(Long userId, CreateFriendApplyRequest request) {
        if (userId.equals(request.getToUserId())) {
            throw new BusinessException("不能添加自己为好友");
        }

        User toUser = userMapper.selectById(request.getToUserId());
        if (toUser == null) {
            throw new BusinessException("用户不存在");
        }

        FriendApply existing = friendApplyMapper.selectByFromUserIdAndToUserId(userId, request.getToUserId());
        if (existing != null) {
            throw new BusinessException("已发送好友申请");
        }

        if (isFriend(userId, request.getToUserId())) {
            throw new BusinessException("已是好友");
        }

        FriendApply apply = new FriendApply();
        apply.setFromUserId(userId);
        apply.setToUserId(request.getToUserId());
        apply.setStatus("pending");
        apply.setMessage(request.getMessage());
        friendApplyMapper.insert(apply);
    }

    @Override
    @Transactional
    public void handle(Long userId, HandleFriendApplyRequest request) {
        FriendApply apply = friendApplyMapper.selectById(request.getId());
        if (apply == null) {
            throw new BusinessException("申请不存在");
        }
        if (!apply.getToUserId().equals(userId)) {
            throw new BusinessException("无权限处理此申请");
        }

        if ("accept".equals(request.getAction())) {
            friendApplyMapper.updateStatus(request.getId(), "accepted");

            Friend friend1 = new Friend();
            friend1.setUserId(apply.getFromUserId());
            friend1.setFriendUserId(apply.getToUserId());
            friendMapper.insert(friend1);

            Friend friend2 = new Friend();
            friend2.setUserId(apply.getToUserId());
            friend2.setFriendUserId(apply.getFromUserId());
            friendMapper.insert(friend2);
        } else if ("reject".equals(request.getAction())) {
            friendApplyMapper.updateStatus(request.getId(), "rejected");
        } else {
            throw new BusinessException("无效操作");
        }
    }

    @Override
    public List<FriendApplyVO> listReceived(Long userId) {
        List<FriendApply> applies = friendApplyMapper.selectByToUserId(userId);
        return applies.stream()
                .map(apply -> {
                    User fromUser = userMapper.selectById(apply.getFromUserId());
                    return convertApplyToVO(apply, fromUser, null);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendApplyVO> listSent(Long userId) {
        List<FriendApply> applies = friendApplyMapper.selectByFromUserId(userId);
        return applies.stream()
                .map(apply -> {
                    User toUser = userMapper.selectById(apply.getToUserId());
                    return convertApplyToVO(apply, null, toUser);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendVO> listFriends(Long userId) {
        List<Friend> friends = friendMapper.selectByUserId(userId);
        return friends.stream()
                .map(friend -> {
                    User friendUser = userMapper.selectById(friend.getFriendUserId());
                    return convertFriendToVO(friend, friendUser);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteFriend(Long userId, Long friendUserId) {
        friendMapper.deleteByUserIdAndFriendUserId(userId, friendUserId);
        friendMapper.deleteByUserIdAndFriendUserId(friendUserId, userId);
    }

    @Override
    public boolean isFriend(Long userId, Long otherUserId) {
        return friendMapper.selectByUserIdAndFriendUserId(userId, otherUserId) != null;
    }

    private FriendApplyVO convertApplyToVO(FriendApply apply, User fromUser, User toUser) {
        FriendApplyVO vo = new FriendApplyVO();
        vo.setId(apply.getId());
        vo.setFromUserId(apply.getFromUserId());
        vo.setToUserId(apply.getToUserId());
        vo.setStatus(apply.getStatus());
        vo.setMessage(apply.getMessage());
        vo.setCreateTime(apply.getCreateTime());

        if (fromUser != null) {
            UserVO userVO = new UserVO();
            userVO.setId(fromUser.getId());
            userVO.setUsername(fromUser.getUsername());
            userVO.setNickname(fromUser.getNickname());
            userVO.setAvatar(fromUser.getAvatar());
            vo.setFromUser(userVO);
        }
        if (toUser != null) {
            UserVO userVO = new UserVO();
            userVO.setId(toUser.getId());
            userVO.setUsername(toUser.getUsername());
            userVO.setNickname(toUser.getNickname());
            userVO.setAvatar(toUser.getAvatar());
            vo.setToUser(userVO);
        }
        return vo;
    }

    private FriendVO convertFriendToVO(Friend friend, User friendUser) {
        FriendVO vo = new FriendVO();
        vo.setId(friend.getId());
        vo.setUserId(friend.getUserId());
        vo.setFriendUserId(friend.getFriendUserId());
        vo.setRemark(friend.getRemark());
        vo.setCreateTime(friend.getCreateTime());

        if (friendUser != null) {
            UserVO userVO = new UserVO();
            userVO.setId(friendUser.getId());
            userVO.setUsername(friendUser.getUsername());
            userVO.setNickname(friendUser.getNickname());
            userVO.setAvatar(friendUser.getAvatar());
            vo.setFriendUser(userVO);
        }
        return vo;
    }
}