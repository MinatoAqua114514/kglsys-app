package com.kglsys.api.response.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 学习路径知识图谱的完整响应体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPathGraphDTO {
    private Long pathId;
    private String pathName;
    private String pathDescription;
    private List<KnowledgeNodeDTO> nodes; // 图谱中的所有节点
    private List<KnowledgeEdgeDTO> edges; // 图谱中节点之间的所有边
}
