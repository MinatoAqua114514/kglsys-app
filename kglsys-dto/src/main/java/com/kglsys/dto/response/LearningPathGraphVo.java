package com.kglsys.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * 用于前端图形化展示学习路径的视图对象。
 * 包含了构建图形所需的所有节点和边。
 */
@Data
@Builder
public class LearningPathGraphVo {
    private Long pathId;
    private String pathName;
    private List<GraphNodeVo> nodes;
    private List<GraphEdgeVo> edges;
}