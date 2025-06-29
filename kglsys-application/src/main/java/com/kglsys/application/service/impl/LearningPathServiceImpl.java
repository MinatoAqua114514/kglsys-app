package com.kglsys.application.service.impl;

import com.kglsys.application.service.LearningPathService;
import com.kglsys.common.exception.BusinessRuleException;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.entity.LearningPath;
import com.kglsys.domain.entity.UserLearningProfile;
import com.kglsys.dto.request.SelectPathRequest;
import com.kglsys.dto.response.LearningPathVo;
import com.kglsys.infra.repository.LearningPathRepository;
import com.kglsys.infra.repository.UserLearningProfileRepository;
import com.kglsys.application.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LearningPathServiceImpl implements LearningPathService {

    private final LearningPathRepository pathRepository;
    private final UserLearningProfileRepository profileRepository;

    // FR7: 根据已确认岗位列出路径
    @Override
    @Transactional(readOnly = true)
    public List<LearningPathVo> getPathsForCurrentUserStyle() {
        Long userId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new IllegalStateException("用户未登录"));
        UserLearningProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleException("请先完成岗位测评。"));

        if (profile.getLearningStyle() == null) {
            throw new BusinessRuleException("请先确认您的目标岗位。");
        }

        Integer styleId = profile.getLearningStyle().getId();
        return pathRepository.findByForStyle_Id(styleId).stream()
                .map(this::mapPathToVo)
                .collect(Collectors.toList());
    }

    // FR8: 选择学习路径
    @Override
    @Transactional
    public void selectLearningPath(SelectPathRequest request) {
        Long userId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new IllegalStateException("用户未登录"));
        UserLearningProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleException("用户档案不存在。"));

        LearningPath path = pathRepository.findById(request.getPathId())
                .orElseThrow(() -> new ResourceNotFoundException("选择的学习路径不存在。"));

        // 校验该路径是否与用户选择的岗位匹配
        if (!path.getLearningStyle().getId().equals(profile.getLearningStyle().getId())) {
            throw new BusinessRuleException("该学习路径不适用于您当前选择的岗位。");
        }

        profile.setLearningPath(path);
        profileRepository.save(profile);
    }

    // ... mapping methods ...
    private LearningPathVo mapPathToVo(LearningPath path) {
        LearningPathVo vo = new LearningPathVo();
        vo.setId(path.getId());
        vo.setName(path.getName());
        vo.setDescription(path.getDescription());
        if (path.getDifficultyLevel() != null) {
            vo.setDifficultyLevel(path.getDifficultyLevel().name());
        }
        return vo;
    }
}