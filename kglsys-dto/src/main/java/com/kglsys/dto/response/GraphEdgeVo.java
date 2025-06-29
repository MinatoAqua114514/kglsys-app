package com.kglsys.dto.response;

import com.kglsys.domain.enums.DependencyType;
import lombok.Builder;
import lombok.Data;

/**
 * 学习路径图中的边，表示节点间的依赖关系。
 */
@Data
@Builder
public class GraphEdgeVo {
    /** 前置节点的ID */
    private Long source;
    /** 依赖（后置）节点的ID */
    private Long target;
    private DependencyType type;
}