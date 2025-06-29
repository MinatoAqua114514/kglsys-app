package com.kglsys.dto.graph;

import com.kglsys.domain.enums.LearningStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraphNodeDTO {
    private Long id; // Corresponds to LearningPathNode ID
    private String title;
    private LearningStatus status;
}