package com.skillexchange.mapper;

import com.skillexchange.entity.SkillExchange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkillExchangeMapper {

    int insert(SkillExchange skillExchange);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    int updateActualTime(@Param("id") Long id, @Param("actualTime") java.time.LocalDateTime actualTime);

    SkillExchange selectById(Long id);

    List<SkillExchange> selectByProviderId(Long providerId);

    List<SkillExchange> selectByRequesterId(Long requesterId);

    List<SkillExchange> selectByStatus(String status);

    int countByProviderId(Long providerId);

    int countByRequesterId(Long requesterId);

    int countByStatus(String status);

    int countAll();
}