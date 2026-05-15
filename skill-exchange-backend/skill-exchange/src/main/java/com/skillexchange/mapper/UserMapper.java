package com.skillexchange.mapper;

import com.skillexchange.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User user);

    int update(User user);

    int deleteById(Long id);

    User selectById(Long id);

    User selectByUsername(String username);

    User selectByEmail(String email);

    List<User> selectAll();

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int updateTimeCoin(@Param("id") Long id, @Param("timeCoin") Integer timeCoin);

    int updateCreditScore(@Param("id") Long id, @Param("creditScore") Integer creditScore);

    List<User> selectByStatus(Integer status);

    List<User> selectByRole(String role);

    int countByStatus(Integer status);

    int countByRole(String role);

    int countAll();
}