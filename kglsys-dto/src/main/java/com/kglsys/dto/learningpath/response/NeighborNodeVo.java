package com.kglsys.dto.learningpath.response;

import lombok.Builder;
import lombok.Data;

/**
 * 表示某个节点的相邻节点（前置或后置）信息。
 */
@Data
@Builder
public class NeighborNodeVo {

    private Long nodeId;
    private String title;
    private NeighborType type;

    public enum NeighborType {
        /** 前置节点 */
        PREREQUISITE,
        /** 后置（依赖）节点 */
        DEPENDENT
    }
}