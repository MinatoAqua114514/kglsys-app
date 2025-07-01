package com.kglsys.application.service.learningpath;

import com.kglsys.dto.learningpath.request.SelectPathRequest;
import com.kglsys.dto.learningpath.response.LearningPathGraphVo;
import com.kglsys.dto.learningpath.response.LearningPathVo;
import com.kglsys.dto.learningpath.response.NodeDetailVo;
import com.kglsys.dto.learningpath.response.NodeKnowledgeGraphVo;

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

    /**
     * 【新增】获取指定学习路径节点关联的知识图谱。
     * @param nodeId 节点的ID
     * @return 包含图谱节点和边的视图对象。
     */
    NodeKnowledgeGraphVo getNodeKnowledgeGraph(Long nodeId);
}
