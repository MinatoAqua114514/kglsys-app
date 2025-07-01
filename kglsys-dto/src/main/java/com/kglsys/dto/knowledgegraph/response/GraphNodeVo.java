package com.kglsys.dto.knowledgegraph.response;

import com.kglsys.domain.learningpath.enums.LearningStatus;
import lombok.Builder;
import lombok.Data;

/**
 * 学习路径图中的节点。
 */
@Data
@Builder
public class GraphNodeVo {
    /** 对应 LearningPathNode 的 ID */
    private Long id;
    private String title;
    private LearningStatus status;
}