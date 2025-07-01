package com.kglsys.application.service.user;

import com.kglsys.domain.learningpath.enums.LearningStatus;

public interface LearningProgressService {

    /**
     * 更新当前用户对指定学习路径节点的状态。
     *
     * @param nodeId The ID of the learning path node.
     * @param newStatus The new status to set for the node.
     */
    void updateNodeStatus(Long nodeId, LearningStatus newStatus);
}