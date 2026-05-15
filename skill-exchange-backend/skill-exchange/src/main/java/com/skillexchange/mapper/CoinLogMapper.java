package com.skillexchange.mapper;

import com.skillexchange.entity.CoinLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CoinLogMapper {

    int insert(CoinLog coinLog);

    List<CoinLog> selectByUserId(Long userId);

    List<CoinLog> selectByUserIdAndType(Long userId, String type);

    int countByUserId(Long userId);
}