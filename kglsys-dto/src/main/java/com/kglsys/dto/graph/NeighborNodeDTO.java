package com.kglsys.dto.graph;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NeighborNodeDTO {
    private Long nodeId;
    private String title;
    private NeighborType type;

    public enum NeighborType {
        PREREQUISITE, // 前置节点
        DEPENDENT   // 后置节点
    }
}