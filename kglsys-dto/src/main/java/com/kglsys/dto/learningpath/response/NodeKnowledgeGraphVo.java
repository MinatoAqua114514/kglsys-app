package com.kglsys.dto.learningpath.response;

import com.kglsys.dto.knowledgegraph.response.KgGraphEdgeVo;
import com.kglsys.dto.knowledgegraph.response.KgGraphNodeVo;
import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * 学习路径节点的知识图谱视图对象。
 * 包含了构建图谱所需的所有节点和边。
 */
@Data
@Builder
public class NodeKnowledgeGraphVo {
    private List<KgGraphNodeVo> nodes;
    private List<KgGraphEdgeVo> edges;
}