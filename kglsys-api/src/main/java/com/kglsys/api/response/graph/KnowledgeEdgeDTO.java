package com.kglsys.api.response.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 知识图谱边的数据传输对象 (用于API响应)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeEdgeDTO {
    private Long sourceNodeId; // 起始节点ID
    private Long targetNodeId; // 目标节点ID
    private String relationType; // 关系类型 (e.g., "PREREQUISITE")
}