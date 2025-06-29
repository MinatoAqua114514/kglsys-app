package com.kglsys.application.service;

import com.kglsys.dto.request.SelectPathRequest;
import com.kglsys.dto.response.LearningPathGraphVo;
import com.kglsys.dto.response.LearningPathVo;
import com.kglsys.dto.response.NodeDetailVo;

import java.util.List;

public interface LearningPathService {
    List<LearningPathVo> getPathsForCurrentUserStyle();
    void selectLearningPath(SelectPathRequest request);

    /**
     * 【新增】获取当前用户所选学习路径的完整图谱信息。
     * @return 包含节点、边和学习状态的图谱视图对象。
     */
    LearningPathGraphVo getLearningPathGraphForCurrentUser();

    /**
     * 【新增】获取指定学习路径节点的详细信息。
     * @param nodeId 节点的ID
     * @return 包含节点详情、资源、题目和邻居的视图对象。
     */
    NodeDetailVo getNodeDetails(Long nodeId);
}
