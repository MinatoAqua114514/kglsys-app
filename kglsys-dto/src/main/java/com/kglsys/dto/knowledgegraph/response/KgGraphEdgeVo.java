package com.kglsys.dto.knowledgegraph.response;

import lombok.Builder;
import lombok.Data;

/**
 * 代表知识图谱中的一条关系边。
 */
@Data
@Builder
public class KgGraphEdgeVo {
    /** 对应 KgRelationship 的数据库 ID */
    private Long id;
    /** 源节点的 ID (KgEntity.id) */
    private Long source;
    /** 目标节点的 ID (KgEntity.id) */
    private Long target;
    /** 关系的标签，用于鼠标悬浮提示 */
    private String label;
}