package com.kglsys.dto.knowledgegraph.response;

import lombok.Builder;
import lombok.Data;

/**
 * 用于实体详情页展示的关联关系信息。
 */
@Data
@Builder
public class KgRelationshipVo {
    private Long relationshipId;
    private String relationType;
    private String description;
    /** 关系中另一个实体的ID */
    private Long relatedEntityId;
    /** 关系中另一个实体的名称 */
    private String relatedEntityName;
}