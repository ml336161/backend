package com.skillexchange.mapper;

import com.skillexchange.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedbackMapper {

    int insert(Feedback feedback);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    int updateReply(@Param("id") Long id, @Param("reply") String reply, @Param("replyTime") java.time.LocalDateTime replyTime);

    Feedback selectById(Long id);

    List<Feedback> selectAll();

    List<Feedback> selectByUserId(Long userId);

    List<Feedback> selectByStatus(String status);

    int countAll();

    int countPending();
}