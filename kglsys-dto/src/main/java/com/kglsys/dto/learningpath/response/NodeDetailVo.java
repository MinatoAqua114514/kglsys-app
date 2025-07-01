package com.kglsys.dto.learningpath.response;

import com.kglsys.domain.learningpath.enums.LearningStatus;
import com.kglsys.dto.knowledgegraph.response.KgEntitySummaryVo;
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
}