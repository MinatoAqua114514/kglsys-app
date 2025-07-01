package com.kglsys.dto.knowledgegraph.response;

import com.kglsys.dto.problem.response.ProblemSummaryVo;
import com.kglsys.dto.resource.response.LearningResourceVo;
import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * 知识实体详情的完整视图对象。
 */
@Data
@Builder
public class KgEntityDetailVo {
    private Long id;
    private String name;
    private String type;
    private String description;

    private List<KgEntityPropertyVo> properties;
    private List<LearningResourceVo> resources;
    private List<ProblemSummaryVo> problems;
    private List<KgRelationshipVo> outgoingRelationships;
    private List<KgRelationshipVo> incomingRelationships;
}