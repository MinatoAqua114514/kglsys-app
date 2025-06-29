package com.kglsys.dto.graph;

import com.kglsys.domain.enums.DependencyType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraphEdgeDTO {
    private Long source; // Prerequisite Node ID
    private Long target; // Dependent Node ID
    private DependencyType type;
}