package com.kglsys.dto.knowledgegraph.response;

import lombok.Builder;
import lombok.Data;

/**
 * 代表知识图谱中的一个实体节点。
 * 用于前端图形化展示。
 */
@Data
@Builder
public class KgGraphNodeVo {
    /** 对应 KgEntity 的数据库 ID */
    private Long id;
    /** 实体名称，作为节点的显示标签 */
    private String name;
    /** 实体类型，可用于前端对节点进行不同样式的渲染 */
    private String type;
    /** 实体描述，用于鼠标悬浮提示 */
    private String description;
}