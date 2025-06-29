package com.kglsys.application.service.impl;

import com.kglsys.application.service.LearningProgressService;
import com.kglsys.application.util.SecurityUtil;
import com.kglsys.common.exception.BusinessRuleException;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.entity.LearningPathNode;
import com.kglsys.domain.entity.User;
import com.kglsys.domain.entity.UserLearningProgress;
import com.kglsys.domain.enums.LearningStatus;
import com.kglsys.infra.repository.LearningPathNodeRepository;
import com.kglsys.infra.repository.UserLearningProgressRepository;
import com.kglsys.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LearningProgressServiceImpl implements LearningProgressService {

    private final UserLearningProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final LearningPathNodeRepository nodeRepository;

    @Override
    @Transactional
    public void updateNodeStatus(Long nodeId, LearningStatus newStatus) {
        Long userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new IllegalStateException("无法获取当前用户信息"));

        // 1. 查找现有的进度记录，如果不存在则创建一个新的
        UserLearningProgress progress = progressRepository.findByUser_IdAndPathNode_Id(userId, nodeId)
                .orElseGet(() -> createNewProgress(userId, nodeId));

        // 2. 根据新状态应用业务规则
        switch (newStatus) {
            case IN_PROGRESS:
                // 只有当状态为 NOT_STARTED 时，才更新为 IN_PROGRESS 并记录开始时间
                if (progress.getStatus() == LearningStatus.NOT_STARTED) {
                    progress.setStatus(LearningStatus.IN_PROGRESS);
                    progress.setStartedAt(LocalDateTime.now());
                }
                break;
            case MASTERED:
                // 只有当状态为 IN_PROGRESS 时，才允许更新为 MASTERED
                if (progress.getStatus() == LearningStatus.IN_PROGRESS) {
                    progress.setStatus(LearningStatus.MASTERED);
                    progress.setCompletedAt(LocalDateTime.now());
                } else if (progress.getStatus() == LearningStatus.NOT_STARTED) {
                    // 允许从 "未开始" 直接跳到 "已掌握"
                    progress.setStatus(LearningStatus.MASTERED);
                    progress.setStartedAt(LocalDateTime.now()); // 认为开始和完成是同时的
                    progress.setCompletedAt(LocalDateTime.now());
                }
                break;
            case NOT_STARTED:
                // 通常不允许将状态重置为 NOT_STARTED，但可以根据业务需求添加逻辑
                throw new BusinessRuleException("不允许将学习状态重置为 '未开始'");
        }

        // 3. 持久化更改
        progressRepository.save(progress);
    }

    /**
     * 辅助方法，用于在用户首次与节点交互时创建进度记录。
     */
    private UserLearningProgress createNewProgress(Long userId, Long nodeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户未找到，ID: " + userId));
        LearningPathNode node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new ResourceNotFoundException("学习节点未找到，ID: " + nodeId));

        return UserLearningProgress.builder()
                .user(user)
                .pathNode(node)
                .status(LearningStatus.NOT_STARTED) // 初始状态
                .build();
    }
}