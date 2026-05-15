package com.skillexchange.service.impl;

import com.skillexchange.dto.LoginRequest;
import com.skillexchange.dto.RegisterRequest;
import com.skillexchange.dto.UpdatePasswordRequest;
import com.skillexchange.dto.UpdateUserRequest;
import com.skillexchange.entity.CoinLog;
import com.skillexchange.entity.User;
import com.skillexchange.exception.BusinessException;
import com.skillexchange.mapper.CoinLogMapper;
import com.skillexchange.mapper.FriendMapper;
import com.skillexchange.mapper.SkillCollectMapper;
import com.skillexchange.mapper.SkillExchangeMapper;
import com.skillexchange.mapper.SkillLikeMapper;
import com.skillexchange.mapper.SkillMapper;
import com.skillexchange.mapper.UserMapper;
import com.skillexchange.service.UserService;
import com.skillexchange.utils.JwtUtil;
import com.skillexchange.vo.CreditRadarVO;
import com.skillexchange.vo.LoginResponse;
import com.skillexchange.vo.ProfileStatsVO;
import com.skillexchange.vo.UserVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SkillMapper skillMapper;

    @Resource
    private CoinLogMapper coinLogMapper;

    @Resource
    private SkillCollectMapper skillCollectMapper;

    @Resource
    private SkillLikeMapper skillLikeMapper;

    @Resource
    private FriendMapper friendMapper;

    @Resource
    private SkillExchangeMapper skillExchangeMapper;

    @Resource
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();











    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("用户名不存在");
        }

        // 明文密码验证（测试用）
        if (!request.getPassword().equals(user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUser(convertToVO(user));
        return response;
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userMapper.selectByUsername(request.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次密码不一致");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());  // 明文保存！不要加密！
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setBirthday(request.getBirthday());
        user.setRole("user");
        user.setTimeCoin(3);
        user.setCreditScore(80);
        user.setStatus(1);
        userMapper.insert(user);

        CoinLog coinLog = new CoinLog();
        coinLog.setUserId(user.getId());
        coinLog.setType("income");
        coinLog.setAmount(3);
        coinLog.setBalance(3);
        coinLog.setDescription("注册赠送时间币");
        coinLogMapper.insert(coinLog);
    }

    @Override
    public UserVO getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    public UserVO getByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    @Transactional
    public UserVO update(Long userId, UpdateUserRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getEmail() != null) {
            User existing = userMapper.selectByEmail(request.getEmail());
            if (existing != null && !existing.getId().equals(userId)) {
                throw new BusinessException("邮箱已被使用");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }
        userMapper.update(user);
        return convertToVO(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        userMapper.updatePassword(userId, passwordEncoder.encode(request.getNewPassword()));
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        userMapper.updateStatus(id, status);
    }

    @Override
    public List<UserVO> listAll() {
        return userMapper.selectAll().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserVO> listByStatus(Integer status) {
        return userMapper.selectByStatus(status).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public User getEntityById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getEntityByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    @Transactional
    public void updateTimeCoin(Long userId, Integer amount) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        int newBalance = user.getTimeCoin() + amount;
        if (newBalance < 0) {
            throw new BusinessException("时间币不足");
        }
        userMapper.updateTimeCoin(userId, newBalance);

        CoinLog coinLog = new CoinLog();
        coinLog.setUserId(userId);
        coinLog.setType(amount > 0 ? "income" : "expense");
        coinLog.setAmount(Math.abs(amount));
        coinLog.setBalance(newBalance);
        coinLogMapper.insert(coinLog);
    }

    @Override
    public CreditRadarVO getCreditRadar(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        CreditRadarVO radar = new CreditRadarVO();
        radar.setSkillCount(skillMapper.countByUserId(userId));
        radar.setTimeCoinBalance(user.getTimeCoin());
        radar.setExchangeCount(0);
        radar.setGoodCommentCount(0);
        radar.setLoginDays(1);

        return radar;
    }

    @Override
    public boolean checkFirstSkill(Long userId) {
        return skillMapper.countByUserId(userId) == 0;
    }

    @Override
    @Transactional
    public void rewardFirstSkill(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        int newBalance = user.getTimeCoin() + 2;
        userMapper.updateTimeCoin(userId, newBalance);

        CoinLog coinLog = new CoinLog();
        coinLog.setUserId(userId);
        coinLog.setType("income");
        coinLog.setAmount(2);
        coinLog.setBalance(newBalance);
        coinLog.setDescription("发布首技能奖励");
        coinLogMapper.insert(coinLog);
    }

    @Override
    public ProfileStatsVO getProfileStats(Long userId) {
        ProfileStatsVO stats = new ProfileStatsVO();
        stats.setCollectCount(skillCollectMapper.countByUserId(userId));
        stats.setLikeCount(skillLikeMapper.countByUserId(userId));
        stats.setFriendCount(friendMapper.countByUserId(userId));
        stats.setSkillCount(skillMapper.countByUserId(userId));
        stats.setAppliedExchangeCount(skillExchangeMapper.countByRequesterId(userId));
        stats.setReceivedExchangeCount(skillExchangeMapper.countByProviderId(userId));
        return stats;
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setBirthday(user.getBirthday());
        vo.setRole(user.getRole());
        vo.setTimeCoin(user.getTimeCoin());
        vo.setCreditScore(user.getCreditScore());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }
}