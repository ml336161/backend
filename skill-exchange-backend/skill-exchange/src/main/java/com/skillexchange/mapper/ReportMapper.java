package com.skillexchange.mapper;

import com.skillexchange.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {

    int insert(Report report);

    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("handleResult") String handleResult,
                     @Param("handleTime") java.time.LocalDateTime handleTime, @Param("handlerId") Long handlerId);

    Report selectById(Long id);

    List<Report> selectAll();

    List<Report> selectByStatus(String status);

    List<Report> selectByReporterId(Long reporterId);

    int countByStatus(String status);

    int countAll();
}