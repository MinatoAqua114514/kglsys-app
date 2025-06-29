package com.kglsys.dto.response;

import com.kglsys.domain.enums.LearningStatus;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class NodeDetailVo {
    private Long id;
    private String title;
    private String description;
    private LearningStatus status;
    private List<NeighborNodeVo> neighbors;
    private List<KgEntitySummaryVo> coreConcepts;
    private List<LearningResourceVo> resources;
    private List<ProblemSummaryVo> problems;
}