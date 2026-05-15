package com.skillexchange.mapper;

import com.skillexchange.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendMapper {

    int insert(Friend friend);

    int deleteByUserIdAndFriendUserId(@Param("userId") Long userId, @Param("friendUserId") Long friendUserId);

    Friend selectByUserIdAndFriendUserId(@Param("userId") Long userId, @Param("friendUserId") Long friendUserId);

    List<Friend> selectByUserId(Long userId);

    int countByUserId(Long userId);

    int updateRemark(@Param("userId") Long userId, @Param("friendUserId") Long friendUserId, @Param("remark") String remark);
}