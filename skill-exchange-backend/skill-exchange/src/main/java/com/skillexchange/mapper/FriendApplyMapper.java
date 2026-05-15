package com.skillexchange.mapper;

import com.skillexchange.entity.FriendApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendApplyMapper {

    int insert(FriendApply friendApply);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    FriendApply selectById(Long id);

    List<FriendApply> selectByFromUserId(Long fromUserId);

    List<FriendApply> selectByToUserId(Long toUserId);

    FriendApply selectByFromUserIdAndToUserId(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    int countByToUserIdAndStatus(@Param("toUserId") Long toUserId, @Param("status") String status);
}