package com.skillexchange.service.impl;

import com.skillexchange.entity.CoinLog;
import com.skillexchange.entity.User;
import com.skillexchange.exception.BusinessException;
import com.skillexchange.mapper.CoinLogMapper;
import com.skillexchange.mapper.UserMapper;
import com.skillexchange.service.CoinService;
import com.skillexchange.vo.CoinLogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoinServiceImpl implements CoinService {

    @Resource
    private CoinLogMapper coinLogMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public void signIn(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 先检查今日是否已签到
        if (checkTodaySigned(userId)) {
            throw new BusinessException("今日已签到");
        }

        // 计算连续签到天数
        int consecutiveDays = calculateConsecutiveDays(userId);

        // 计算奖励
        int reward = 1;
        if (consecutiveDays >= 7) {
            reward = 3;
        } else if (consecutiveDays >= 3) {
            reward = 2;
        }

        // 更新时间币
        int newBalance = user.getTimeCoin() + reward;
        userMapper.updateTimeCoin(userId, newBalance);

        // 记录流水（description必须包含"签到"关键字！）
        CoinLog coinLog = new CoinLog();
        coinLog.setUserId(userId);
        coinLog.setType("income");
        coinLog.setAmount(reward);
        coinLog.setBalance(newBalance);
        coinLog.setRelatedType("sign_in");
        coinLog.setDescription("每日签到奖励（第" + (consecutiveDays + 1) + "天）");
        coinLogMapper.insert(coinLog);
    }

    /**
     * 计算连续签到天数
     */
    private int calculateConsecutiveDays(Long userId) {
        List<CoinLog> logs = coinLogMapper.selectByUserIdAndType(userId, "income");

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        int consecutive = 0;

        for (int i = logs.size() - 1; i >= 0; i--) {
            CoinLog log = logs.get(i);
            if (log.getDescription() != null && log.getDescription().startsWith("每日签到")) {
                if (log.getCreateTime().isAfter(yesterday)) {
                    consecutive++;
                    yesterday = yesterday.minusDays(1);
                } else {
                    break;
                }
            }
        }

        return consecutive;
    }

    @Override
    public List<CoinLogVO> getLogs(Long userId) {
        List<CoinLog> logs = coinLogMapper.selectByUserId(userId);
        return logs.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkTodaySigned(Long userId) {
        List<CoinLog> logs = coinLogMapper.selectByUserIdAndType(userId, "income");

        // 获取今天的开始时间（0点0分0秒）
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        // 获取今天的结束时间（23点59分59秒）
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        // 遍历日志，查找今天的签到记录
        for (CoinLog log : logs) {
            LocalDateTime logTime = log.getCreateTime();
            if (logTime != null) {
                // 检查时间是否在今天范围内
                boolean isToday = !logTime.isBefore(todayStart) && !logTime.isAfter(todayEnd);

                // 检查是否是签到记录
                boolean isSignIn = log.getDescription() != null && log.getDescription().contains("签到");

                if (isToday && isSignIn) {
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public int getConsecutiveDays(Long userId) {
        List<CoinLog> logs = coinLogMapper.selectByUserIdAndType(userId, "income");

        // 首先检查今天是否签到
        boolean todaySigned = checkTodaySigned(userId);
        
        // 如果今天没签到，连续签到天数为0（因为断签了）
        if (!todaySigned) {
            return 0;
        }

        // 从昨天开始检查，计算之前连续签到了多少天
        LocalDateTime checkDate = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        int consecutive = 1; // 今天已经签到，至少1天

        // 正序遍历日志（数据库已经按 create_time DESC 排序，所以 logs.get(0) 是最新的）
        for (int i = 0; i < logs.size(); i++) {
            CoinLog log = logs.get(i);
            // 只计算签到记录
            if (log.getDescription() != null && log.getDescription().contains("签到")) {
                // 检查这条记录是否是我们正在查找的那天
                if (log.getCreateTime().isAfter(checkDate) && log.getCreateTime().isBefore(checkDate.plusDays(1))) {
                    consecutive++;
                    checkDate = checkDate.minusDays(1);
                } else if (log.getCreateTime().isBefore(checkDate)) {
                    // 如果记录比检查日期早，说明连续签到中断
                    break;
                }
            }
        }
        return consecutive;
    }

    private CoinLogVO convertToVO(CoinLog log) {
        CoinLogVO vo = new CoinLogVO();
        vo.setId(log.getId());
        vo.setUserId(log.getUserId());
        vo.setType(log.getType());
        vo.setAmount(log.getAmount());
        vo.setBalance(log.getBalance());
        vo.setRelatedType(log.getRelatedType());
        vo.setRelatedId(log.getRelatedId());
        vo.setDescription(log.getDescription());
        vo.setCreateTime(log.getCreateTime());
        return vo;
    }
}