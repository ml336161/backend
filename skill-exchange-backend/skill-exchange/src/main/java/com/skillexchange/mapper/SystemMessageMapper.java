package com.skillexchange.mapper;

import com.skillexchange.entity.SystemMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemMessageMapper {

    int insert(SystemMessage message);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int updateAllStatusByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    List<SystemMessage> selectByUserId(Long userId);

    int countUnread(Long userId);

    int deleteById(Long id);
}
