package com.kglsys.dto.graph;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class LearningPathGraphDTO {
    private Long pathId;
    private String pathName;
    private List<GraphNodeDTO> nodes;
    private List<GraphEdgeDTO> edges;
}