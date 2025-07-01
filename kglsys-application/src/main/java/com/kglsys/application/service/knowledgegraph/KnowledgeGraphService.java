package com.kglsys.application.service.knowledgegraph;

import com.kglsys.dto.knowledgegraph.response.KgEntityDetailVo;

public interface KnowledgeGraphService {
    /**
     * 获取知识实体的完整详细信息。
     * @param entityId 实体的ID
     * @return 包含所有关联信息的实体详情视图对象。
     */
    KgEntityDetailVo getEntityDetails(Long entityId);
}