package com.kglsys.application.service;

import com.kglsys.common.exception.config.ResourceNotFoundException;
import com.kglsys.domain.entity.graph.KnowledgeNodeEntity;
import com.kglsys.domain.entity.graph.LearningPathItemEntity;
import com.kglsys.domain.entity.learning.LearningPathEntity;
import com.kglsys.domain.repository.graph.KnowledgeGraphEdgeRepository;
import com.kglsys.domain.repository.learning.LearningPathRepository;
import com.kglsys.api.response.graph.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 知识图谱相关服务
 */
@Service
@RequiredArgsConstructor
public class GraphService {

    private final LearningPathRepository learningPathRepository;
    private final KnowledgeGraphEdgeRepository knowledgeGraphEdgeRepository;

    /**
     * 根据学习路径ID获取用于构建知识图谱所需的数据
     * @param pathId 学习路径的ID
     * @return 包含节点和边的图谱数据DTO
     */
    @Transactional(readOnly = true)
    public LearningPathGraphDTO getGraphDataForLearningPath(Long pathId) {
        // 1. 查询学习路径
        LearningPathEntity path = learningPathRepository.findById(pathId)
                .orElseThrow(() -> new ResourceNotFoundException("LearningPath not found with id: " + pathId));

        // 2. 获取路径中的所有知识节点
        Set<KnowledgeNodeEntity> nodesInPath = path.getItems().stream()
                .map(LearningPathItemEntity::getNode)
                .collect(Collectors.toSet());

        if (nodesInPath.isEmpty()) {
            return LearningPathGraphDTO.builder()
                    .pathId(path.getId())
                    .pathName(path.getName())
                    .pathDescription(path.getDescription())
                    .nodes(Collections.emptyList())
                    .edges(Collections.emptyList())
                    .build();
        }

        // 3. 提取节点ID
        Set<Long> nodeIds = nodesInPath.stream()
                .map(KnowledgeNodeEntity::getId)
                .collect(Collectors.toSet());

        // 4. 查询边信息
        List<KnowledgeEdgeDTO> edgesInPath = knowledgeGraphEdgeRepository.findEdgesConnectingNodes(nodeIds)
                .stream()
                .map(edge -> new KnowledgeEdgeDTO(
                        edge.getSourceNode().getId(),
                        edge.getTargetNode().getId(),
                        edge.getRelationType()
                ))
                .collect(Collectors.toList());

        // 5. 构造节点DTO
        List<KnowledgeNodeDTO> nodeDTOs = nodesInPath.stream()
                .map(node -> new KnowledgeNodeDTO(
                        node.getId(),
                        node.getName(),
                        node.getDescription(),
                        node.getContentType()
                ))
                .collect(Collectors.toList());

        // 6. 返回图谱数据
        return LearningPathGraphDTO.builder()
                .pathId(path.getId())
                .pathName(path.getName())
                .pathDescription(path.getDescription())
                .nodes(nodeDTOs)
                .edges(edgesInPath)
                .build();
    }
}

/*
@Transactional(readOnly = true)
public LearningPathGraphDTO getGraphDataForLearningPath(Long pathId) {
    // 1. 获取学习路径实体
    LearningPathEntity path = learningPathRepository.findById(pathId)
            .orElseThrow(() -> new ResourceNotFoundException("LearningPath not found with id: " + pathId));

    // 2. 获取路径内所有原始知识节点
    Set<KnowledgeNodeEntity> pathNodes = path.getItems().stream()
            .map(item -> item.getNode())
            .collect(Collectors.toSet());

    if (pathNodes.isEmpty()) {
        // 如果路径下没有节点，直接返回空图
        return buildEmptyGraphDTO(path);
    }

    // 3. 获取路径内所有节点的ID
    Set<Long> pathNodeIds = pathNodes.stream()
            .map(KnowledgeNodeEntity::getId)
            .collect(Collectors.toSet());

    // 4. 使用更新后的方法，获取所有从路径内节点出发的边
    List<KnowledgeEdgeDTO> allOutgoingEdges = knowledgeGraphEdgeRepository.findBySourceNodeIdIn(pathNodeIds)
            .stream()
            .map(edge -> new KnowledgeEdgeDTO(
                    edge.getSourceNode().getId(),
                    edge.getTargetNode().getId(),
                    edge.getRelationType()
            ))
            .collect(Collectors.toList());

    // 5. 建立一个包含所有相关节点的Map，用于去重和最终转换
    // 首先将路径内的节点全部放入Map
    Map<Long, KnowledgeNodeEntity> allNodesMap = pathNodes.stream()
            .collect(Collectors.toMap(KnowledgeNodeEntity::getId, Function.identity()));

    // 6. 遍历所有出边，将被引用的、但不在路径内的“外部节点”也加入到Map中
    // 注意：由于实体关联是Lazy的，访问 edge.getTargetNode() 会触发数据库查询。
    // Hibernate的Session级缓存会确保同一个外部节点只被查询一次。
    knowledgeGraphEdgeRepository.findBySourceNodeIdIn(pathNodeIds).forEach(edge -> {
        allNodesMap.putIfAbsent(edge.getTargetNode().getId(), edge.getTargetNode());
    });

    // 7. 将Map中所有节点转换为DTO
    List<KnowledgeNodeDTO> allNodeDTOs = allNodesMap.values().stream()
            .map(node -> new KnowledgeNodeDTO(
                    node.getId(),
                    node.getName(),
                    node.getDescription(),
                    node.getContentType()
            ))
            .collect(Collectors.toList());

    // 8. 组装最终的响应DTO并返回
    return LearningPathGraphDTO.builder()
            .pathId(path.getId())
            .pathName(path.getName())
            .pathDescription(path.getDescription())
            .nodes(allNodeDTOs)
            .edges(allOutgoingEdges)
            .build();
}

private LearningPathGraphDTO buildEmptyGraphDTO(LearningPathEntity path) {
    return LearningPathGraphDTO.builder()
            .pathId(path.getId())
            .pathName(path.getName())
            .pathDescription(path.getDescription())
            .nodes(Collections.emptyList())
            .edges(Collections.emptyList())
            .build();
}*/
