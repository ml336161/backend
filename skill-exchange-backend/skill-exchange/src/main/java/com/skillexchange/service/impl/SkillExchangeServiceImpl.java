package com.skillexchange.service.impl;

import com.skillexchange.dto.CreateSkillExchangeRequest;
import com.skillexchange.dto.HandleSkillExchangeRequest;
import com.skillexchange.entity.Skill;
import com.skillexchange.entity.SkillExchange;
import com.skillexchange.entity.User;
import com.skillexchange.exception.BusinessException;
import com.skillexchange.mapper.SkillExchangeMapper;
import com.skillexchange.mapper.SkillMapper;
import com.skillexchange.mapper.UserMapper;
import com.skillexchange.service.SkillExchangeService;
import com.skillexchange.service.UserService;
import com.skillexchange.vo.SkillExchangeVO;
import com.skillexchange.vo.SkillVO;
import com.skillexchange.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillExchangeServiceImpl implements SkillExchangeService {

    @Resource
    private SkillExchangeMapper skillExchangeMapper;

    @Resource
    private SkillMapper skillMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SkillExchangeVO create(Long userId, CreateSkillExchangeRequest request) {
        Skill skill = skillMapper.selectById(request.getSkillId());
        if (skill == null) {
            throw new BusinessException("技能不存在");
        }
        if (skill.getUserId().equals(userId)) {
            throw new BusinessException("不能申请自己的技能");
        }

        User provider = userMapper.selectById(skill.getUserId());
        User requester = userMapper.selectById(userId);
        if (provider == null) {
            throw new BusinessException("服务提供者不存在");
        }
        if (requester == null) {
            throw new BusinessException("请求者不存在");
        }

        if (requester.getTimeCoin() < skill.getPrice()) {
            throw new BusinessException("您的时间币不足，需要 " + skill.getPrice() + " 时间币");
        }

        SkillExchange exchange = new SkillExchange();
        exchange.setSkillId(request.getSkillId());
        exchange.setProviderId(skill.getUserId());
        exchange.setRequesterId(userId);
        exchange.setStatus("pending");
        exchange.setPrice(skill.getPrice());

        if (request.getAppointmentTime() != null && !request.getAppointmentTime().trim().isEmpty()) {
            parseAndSetScheduledTime(exchange, request.getAppointmentTime().trim());
        }
        exchange.setRemark(request.getRemark());

        skillExchangeMapper.insert(exchange);

        return convertToVO(exchange, skill, provider, requester);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handle(Long userId, HandleSkillExchangeRequest request) {
        SkillExchange exchange = skillExchangeMapper.selectById(request.getId());
        if (exchange == null) {
            throw new BusinessException("交换申请不存在");
        }

        if (!exchange.getProviderId().equals(userId)) {
            throw new BusinessException("无权限处理此申请");
        }

        String action = request.getAction();
        if ("accept".equals(action)) {
            handleAccept(exchange);
        } else if ("reject".equals(action)) {
            handleReject(exchange.getId());
        } else if ("cancel".equals(action)) {
            handleCancel(exchange, userId);
        } else {
            throw new BusinessException("无效操作: " + action);
        }
    }

    private void handleAccept(SkillExchange exchange) {
        if (!"pending".equals(exchange.getStatus())) {
            throw new BusinessException("该申请已处理");
        }

        User requester = userMapper.selectById(exchange.getRequesterId());
        if (requester.getTimeCoin() < exchange.getPrice()) {
            throw new BusinessException("对方时间币不足");
        }

        userService.updateTimeCoin(exchange.getRequesterId(), -exchange.getPrice());
        userService.updateTimeCoin(exchange.getProviderId(), exchange.getPrice());

        skillExchangeMapper.updateStatus(exchange.getId(), "completed");
        skillExchangeMapper.updateActualTime(exchange.getId(), LocalDateTime.now());
    }

    private void handleReject(Long exchangeId) {
        skillExchangeMapper.updateStatus(exchangeId, "rejected");
    }

    private void handleCancel(SkillExchange exchange, Long userId) {
        if (!"pending".equals(exchange.getStatus())) {
            throw new BusinessException("只能取消待处理的申请");
        }
        if (!exchange.getRequesterId().equals(userId)) {
            throw new BusinessException("无权限取消此申请");
        }
        skillExchangeMapper.updateStatus(exchange.getId(), "cancelled");
    }

    @Override
    public SkillExchangeVO getById(Long id) {
        SkillExchange exchange = skillExchangeMapper.selectById(id);
        if (exchange == null) {
            throw new BusinessException("交换记录不存在");
        }
        Skill skill = skillMapper.selectById(exchange.getSkillId());
        User provider = userMapper.selectById(exchange.getProviderId());
        User requester = userMapper.selectById(exchange.getRequesterId());
        return convertToVO(exchange, skill, provider, requester);
    }

    @Override
    public List<SkillExchangeVO> listReceived(Long userId) {
        List<SkillExchange> exchanges = skillExchangeMapper.selectByProviderId(userId);
        return exchanges.stream()
                .map(exchange -> {
                    Skill skill = skillMapper.selectById(exchange.getSkillId());
                    User provider = userMapper.selectById(exchange.getProviderId());
                    User requester = userMapper.selectById(exchange.getRequesterId());
                    return convertToVO(exchange, skill, provider, requester);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SkillExchangeVO> listSent(Long userId) {
        List<SkillExchange> exchanges = skillExchangeMapper.selectByRequesterId(userId);
        return exchanges.stream()
                .map(exchange -> {
                    Skill skill = skillMapper.selectById(exchange.getSkillId());
                    User provider = userMapper.selectById(exchange.getProviderId());
                    User requester = userMapper.selectById(exchange.getRequesterId());
                    return convertToVO(exchange, skill, provider, requester);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Long countPending(Long userId) {
        List<SkillExchange> exchanges = skillExchangeMapper.selectByProviderId(userId);
        return exchanges.stream()
                .filter(e -> "pending".equals(e.getStatus()))
                .count();
    }

    private void parseAndSetScheduledTime(SkillExchange exchange, String timeStr) {
        try {
            exchange.setScheduledTime(LocalDateTime.parse(timeStr, DATE_TIME_FORMATTER));
        } catch (DateTimeParseException e) {
            try {
                exchange.setScheduledTime(LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } catch (DateTimeParseException e2) {
                throw new BusinessException("日期格式不正确，请使用 yyyy-MM-dd HH:mm:ss 格式: " + timeStr);
            }
        }
    }

    private SkillExchangeVO convertToVO(SkillExchange exchange, Skill skill, User provider, User requester) {
        SkillExchangeVO vo = new SkillExchangeVO();
        vo.setId(exchange.getId());
        vo.setSkillId(exchange.getSkillId());
        vo.setProviderId(exchange.getProviderId());
        vo.setRequesterId(exchange.getRequesterId());
        vo.setStatus(exchange.getStatus());
        vo.setPrice(exchange.getPrice());
        vo.setScheduledTime(exchange.getScheduledTime());
        vo.setActualTime(exchange.getActualTime());
        vo.setRemark(exchange.getRemark());
        vo.setCreateTime(exchange.getCreateTime());

        if (skill != null) {
            SkillVO skillVO = new SkillVO();
            skillVO.setId(skill.getId());
            skillVO.setTitle(skill.getTitle());
            skillVO.setDescription(skill.getDescription());
            vo.setSkill(skillVO);
        }

        if (provider != null) {
            UserVO providerVO = new UserVO();
            providerVO.setId(provider.getId());
            providerVO.setUsername(provider.getUsername());
            providerVO.setNickname(provider.getNickname());
            providerVO.setAvatar(provider.getAvatar());
            vo.setProvider(providerVO);
        }

        if (requester != null) {
            UserVO requesterVO = new UserVO();
            requesterVO.setId(requester.getId());
            requesterVO.setUsername(requester.getUsername());
            requesterVO.setNickname(requester.getNickname());
            requesterVO.setAvatar(requester.getAvatar());
            vo.setRequester(requesterVO);
        }

        return vo;
    }
}