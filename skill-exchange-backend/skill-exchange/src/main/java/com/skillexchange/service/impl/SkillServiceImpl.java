package com.skillexchange.service.impl;

import com.skillexchange.dto.CreateSkillRequest;
import com.skillexchange.dto.UpdateSkillRequest;
import com.skillexchange.entity.Skill;
import com.skillexchange.entity.SkillCollect;
import com.skillexchange.entity.SkillLike;
import com.skillexchange.entity.User;
import com.skillexchange.exception.BusinessException;
import com.skillexchange.mapper.SkillCollectMapper;
import com.skillexchange.mapper.SkillLikeMapper;
import com.skillexchange.mapper.SkillMapper;
import com.skillexchange.mapper.UserMapper;
import com.skillexchange.service.SkillService;
import com.skillexchange.service.UserService;
import com.skillexchange.vo.SkillListResponse;
import com.skillexchange.vo.SkillVO;
import com.skillexchange.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    @Resource
    private SkillMapper skillMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private SkillLikeMapper skillLikeMapper;

    @Resource
    private SkillCollectMapper skillCollectMapper;

    @Resource
    private UserService userService;

    @Override
    @Transactional
    public SkillVO create(Long userId, CreateSkillRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        boolean isFirstSkill = userService.checkFirstSkill(userId);

        Skill skill = new Skill();
        skill.setUserId(userId);
        skill.setTypeId(request.getTypeId());
        skill.setTitle(request.getTitle());
        skill.setDescription(request.getDescription());
        skill.setImages(request.getImages());
        skill.setPrice(request.getPrice() != null ? request.getPrice() : 1);
        skill.setDuration(request.getDuration());
        skill.setLocation(request.getLocation());
        skill.setStatus(1);
        skillMapper.insert(skill);

        if (isFirstSkill) {
            userService.rewardFirstSkill(userId);
        }

        return convertToVO(skill, user, false, false);
    }

    @Override
    @Transactional
    public SkillVO update(Long userId, UpdateSkillRequest request) {
        Skill skill = skillMapper.selectById(request.getId());
        if (skill == null) {
            throw new BusinessException("技能不存在");
        }
        if (!skill.getUserId().equals(userId)) {
            throw new BusinessException("无权限修改此技能");
        }

        if (request.getTypeId() != null) {
            skill.setTypeId(request.getTypeId());
        }
        if (request.getTitle() != null) {
            skill.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            skill.setDescription(request.getDescription());
        }
        if (request.getImages() != null) {
            skill.setImages(request.getImages());
        }
        if (request.getPrice() != null) {
            skill.setPrice(request.getPrice());
        }
        if (request.getDuration() != null) {
            skill.setDuration(request.getDuration());
        }
        if (request.getLocation() != null) {
            skill.setLocation(request.getLocation());
        }
        skillMapper.update(skill);

        User user = userMapper.selectById(userId);
        return convertToVO(skill, user, false, false);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Skill skill = skillMapper.selectById(id);
        if (skill == null) {
            throw new BusinessException("技能不存在");
        }
        skillMapper.deleteById(id);
    }

    @Override
    public SkillVO getById(Long id) {
        Skill skill = skillMapper.selectById(id);
        if (skill == null) {
            throw new BusinessException("技能不存在");
        }
        User user = userMapper.selectById(skill.getUserId());
        return convertToVO(skill, user, false, false);
    }

    @Override
    public SkillVO getByIdWithUser(Long id) {
        Skill skill = skillMapper.selectById(id);
        if (skill == null) {
            throw new BusinessException("技能不存在");
        }
        skillMapper.updateViewCount(id);
        User user = userMapper.selectById(skill.getUserId());
        return convertToVO(skill, user, false, false);
    }

    @Override
    public SkillListResponse list(Integer pageNum, Integer pageSize, Long typeId, String keyword) {
        int offset = (pageNum - 1) * pageSize;
        List<Skill> skills = skillMapper.selectPage(typeId, keyword, offset, pageSize);
        Long total = (long) skillMapper.countPage(typeId, keyword);

        SkillListResponse response = new SkillListResponse();
        response.setList(skills.stream()
                .map(skill -> {
                    User user = userMapper.selectById(skill.getUserId());
                    return convertToVO(skill, user, false, false);
                })
                .collect(Collectors.toList()));
        response.setTotal(total);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        return response;
    }

    @Override
    public List<SkillVO> listByUserId(Long userId) {
        List<Skill> skills = skillMapper.selectByUserId(userId);
        User user = userMapper.selectById(userId);
        return skills.stream()
                .map(skill -> convertToVO(skill, user, false, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void like(Long userId, Long skillId) {
        SkillLike existing = skillLikeMapper.selectByUserIdAndSkillId(userId, skillId);
        if (existing != null) {
            skillLikeMapper.deleteByUserIdAndSkillId(userId, skillId);
            Skill skill = skillMapper.selectById(skillId);
            if (skill != null) {
                skillMapper.updateLikeCount(skillId, skill.getLikeCount() - 1);
            }
        } else {
            SkillLike like = new SkillLike();
            like.setUserId(userId);
            like.setSkillId(skillId);
            skillLikeMapper.insert(like);
            Skill skill = skillMapper.selectById(skillId);
            if (skill != null) {
                skillMapper.updateLikeCount(skillId, skill.getLikeCount() + 1);
            }
        }
    }

    @Override
    @Transactional
    public void collect(Long userId, Long skillId) {
        SkillCollect existing = skillCollectMapper.selectByUserIdAndSkillId(userId, skillId);
        if (existing != null) {
            skillCollectMapper.deleteByUserIdAndSkillId(userId, skillId);
            Skill skill = skillMapper.selectById(skillId);
            if (skill != null) {
                skillMapper.updateCollectCount(skillId, skill.getCollectCount() - 1);
            }
        } else {
            SkillCollect collect = new SkillCollect();
            collect.setUserId(userId);
            collect.setSkillId(skillId);
            skillCollectMapper.insert(collect);
            Skill skill = skillMapper.selectById(skillId);
            if (skill != null) {
                skillMapper.updateCollectCount(skillId, skill.getCollectCount() + 1);
            }
        }
    }

    @Override
    public void updateViewCount(Long id) {
        skillMapper.updateViewCount(id);
    }

    @Override
    public Skill getEntityById(Long id) {
        return skillMapper.selectById(id);
    }

    @Override
    public Long count() {
        return (long) skillMapper.countAll();
    }

    @Override
    public List<SkillVO> listHotSkills(Integer limit) {
        List<Skill> skills = skillMapper.selectHotSkills(limit);
        return skills.stream()
                .map(skill -> {
                    User user = userMapper.selectById(skill.getUserId());
                    return convertToVO(skill, user, false, false);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SkillVO> listLatestSkills(Integer limit) {
        List<Skill> skills = skillMapper.selectLatestSkills(limit);
        return skills.stream()
                .map(skill -> {
                    User user = userMapper.selectById(skill.getUserId());
                    return convertToVO(skill, user, false, false);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SkillVO> listUserCollects(Long userId) {
        List<SkillCollect> collects = skillCollectMapper.selectByUserId(userId);
        if (collects.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> skillIds = collects.stream()
                .map(SkillCollect::getSkillId)
                .collect(Collectors.toList());
        List<Skill> skills = skillMapper.selectByIds(skillIds);
        return skills.stream()
                .map(skill -> {
                    User user = userMapper.selectById(skill.getUserId());
                    return convertToVO(skill, user, false, true);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SkillVO> listUserLikes(Long userId) {
        List<SkillLike> likes = skillLikeMapper.selectByUserId(userId);
        if (likes.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> skillIds = likes.stream()
                .map(SkillLike::getSkillId)
                .collect(Collectors.toList());
        List<Skill> skills = skillMapper.selectByIds(skillIds);
        return skills.stream()
                .map(skill -> {
                    User user = userMapper.selectById(skill.getUserId());
                    return convertToVO(skill, user, true, false);
                })
                .collect(Collectors.toList());
    }

    @Override
    public int countUserCollects(Long userId) {
        return skillCollectMapper.countByUserId(userId);
    }

    @Override
    public int countUserLikes(Long userId) {
        return skillLikeMapper.countByUserId(userId);
    }

    @Override
    public List<SkillVO> listUserCollectsByUserId(Long userId) {
        return listUserCollects(userId);
    }

    @Override
    public List<SkillVO> listUserLikesByUserId(Long userId) {
        return listUserLikes(userId);
    }

    private SkillVO convertToVO(Skill skill, User user, Boolean liked, Boolean collected) {
        SkillVO vo = new SkillVO();
        vo.setId(skill.getId());
        vo.setUserId(skill.getUserId());
        vo.setTypeId(skill.getTypeId());
        vo.setTitle(skill.getTitle());
        vo.setDescription(skill.getDescription());
        vo.setImages(skill.getImages());
        vo.setPrice(skill.getPrice());
        vo.setDuration(skill.getDuration());
        vo.setLocation(skill.getLocation());
        vo.setViewCount(skill.getViewCount());
        vo.setLikeCount(skill.getLikeCount());
        vo.setCollectCount(skill.getCollectCount());
        vo.setStatus(skill.getStatus());
        vo.setLiked(liked);
        vo.setCollected(collected);
        vo.setCreateTime(skill.getCreateTime());

        if (user != null) {
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setUsername(user.getUsername());
            userVO.setNickname(user.getNickname());
            userVO.setAvatar(user.getAvatar());
            userVO.setTimeCoin(user.getTimeCoin());
            userVO.setCreditScore(user.getCreditScore());
            vo.setUser(userVO);
        }
        return vo;
    }
}