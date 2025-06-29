package com.kglsys.dto.nodedetail;

import com.kglsys.domain.enums.LearningStatus;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PathNodeDetailDTO {
    private Long id;
    private String title;
    private String description;
    private LearningStatus status;
    private List<ResourceSummaryDTO> resources;
    private List<ProblemSummaryDTO> problems;
}