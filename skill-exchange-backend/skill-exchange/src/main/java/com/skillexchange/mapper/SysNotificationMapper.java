package com.skillexchange.mapper;

import com.skillexchange.entity.SysNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysNotificationMapper {

    int insert(SysNotification sysNotification);

    int updateIsRead(@Param("id") Long id, @Param("isRead") Integer isRead);

    int updateAllRead(Long userId);

    SysNotification selectById(Long id);

    List<SysNotification> selectByUserId(Long userId);

    List<SysNotification> selectUnreadByUserId(Long userId);

    int countUnreadByUserId(Long userId);
}