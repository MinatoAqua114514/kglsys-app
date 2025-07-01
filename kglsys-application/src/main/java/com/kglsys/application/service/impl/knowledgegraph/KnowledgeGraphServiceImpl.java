package com.kglsys.application.service.impl.knowledgegraph;

import com.kglsys.application.mapper.knowledgegraph.KnowledgeGraphMapper;
import com.kglsys.application.service.knowledgegraph.KnowledgeGraphService;
import com.kglsys.common.exception.ResourceNotFoundException;
import com.kglsys.domain.knowledgegraph.KgEntity;
import com.kglsys.dto.knowledgegraph.response.KgEntityDetailVo;
import com.kglsys.infra.repository.knowledgegraph.KgEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KnowledgeGraphServiceImpl implements KnowledgeGraphService {

    private final KgEntityRepository entityRepository;
    private final KnowledgeGraphMapper knowledgeGraphMapper;

    @Override
    @Transactional(readOnly = true)
    public KgEntityDetailVo getEntityDetails(Long entityId) {
        // 1. 使用我们新创建的高效查询方法
        KgEntity entity = entityRepository.findWithDetailsById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("知识实体未找到，ID: " + entityId));

        // 2. 使用 Mapper 将加载好的完整实体转换为 VO
        return knowledgeGraphMapper.toDetailVo(entity);
    }
}