package com.skillexchange.mapper;

import com.skillexchange.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysLogMapper {

    int insert(SysLog sysLog);

    List<SysLog> selectAll();

    List<SysLog> selectByUserId(Long userId);

    List<SysLog> selectByModule(String module);

    int countAll();
}