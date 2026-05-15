package com.skillexchange.service.impl;

import com.skillexchange.entity.SkillType;
import com.skillexchange.exception.BusinessException;
import com.skillexchange.mapper.SkillTypeMapper;
import com.skillexchange.service.SkillTypeService;
import com.skillexchange.vo.SkillTypeVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillTypeServiceImpl implements SkillTypeService {

    @Resource
    private SkillTypeMapper skillTypeMapper;

    @Override
    public SkillTypeVO getById(Long id) {
        SkillType type = skillTypeMapper.selectById(id);
        if (type == null) {
            throw new BusinessException("技能类型不存在");
        }
        return convertToVO(type);
    }

    @Override
    public List<SkillTypeVO> listAll() {
        return skillTypeMapper.selectAll().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SkillTypeVO> listActive() {
        return skillTypeMapper.selectActive().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public SkillType getEntityById(Long id) {
        return skillTypeMapper.selectById(id);
    }

    private SkillTypeVO convertToVO(SkillType type) {
        SkillTypeVO vo = new SkillTypeVO();
        vo.setId(type.getId());
        vo.setName(type.getName());
        vo.setDescription(type.getDescription());
        vo.setIcon(type.getIcon());
        vo.setSort(type.getSort());
        vo.setStatus(type.getStatus());
        vo.setCreateTime(type.getCreateTime());
        return vo;
    }
}