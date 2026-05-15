package com.skillexchange.service;



import com.skillexchange.dto.LoginRequest;
import com.skillexchange.dto.RegisterRequest;
import com.skillexchange.dto.UpdatePasswordRequest;
import com.skillexchange.dto.UpdateUserRequest;
import com.skillexchange.entity.User;
import com.skillexchange.vo.CreditRadarVO;
import com.skillexchange.vo.LoginResponse;
import com.skillexchange.vo.UserVO;

import java.util.List;

public interface UserService {

    LoginResponse login(LoginRequest request);

    void register(RegisterRequest request);

    UserVO getById(Long id);

    UserVO getByUsername(String username);

    UserVO update(Long userId, UpdateUserRequest request);

    void updatePassword(Long userId, UpdatePasswordRequest request);

    void updateStatus(Long id, Integer status);

    List<UserVO> listAll();

    List<UserVO> listByStatus(Integer status);

    User getEntityById(Long id);

    User getEntityByUsername(String username);

    void updateTimeCoin(Long userId, Integer amount);

    CreditRadarVO getCreditRadar(Long userId);

    boolean checkFirstSkill(Long userId);

    void rewardFirstSkill(Long userId);










}