package com.kglsys.application.mapper;

import com.kglsys.domain.entity.LearningPath;
import com.kglsys.domain.entity.LearningPathNode;
import com.kglsys.domain.entity.PathNodeDependency;
import com.kglsys.domain.enums.LearningStatus;
import com.kglsys.dto.response.GraphEdgeVo;
import com.kglsys.dto.response.GraphNodeVo;
import com.kglsys.dto.response.LearningPathGraphVo;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface LearningPathGraphMapper {

    /**
     * 主映射方法。
     * @param path 包含完整节点和依赖关系的 LearningPath 实体。
     * @param progressMap 一个映射，Key=nodeId, Value=LearningStatus。
     * @return 用于前端展示的 LearningPathGraphVo。
     */
    @Mapping(source = "path.id", target = "pathId")
    @Mapping(source = "path.name", target = "pathName")
    @Mapping(target = "nodes", ignore = true) // 节点和边将通过 @AfterMapping 手动构建
    @Mapping(target = "edges", ignore = true)
    LearningPathGraphVo toGraphVo(LearningPath path, @Context Map<Long, LearningStatus> progressMap);

    /**
     * 在主映射完成后，构建节点和边列表。
     */
    @AfterMapping
    default void afterMapping(@MappingTarget LearningPathGraphVo vo, LearningPath path,
                              @Context Map<Long, LearningStatus> progressMap) {
        if (path == null || path.getNodes() == null) {
            vo.setNodes(Collections.emptyList());
            vo.setEdges(Collections.emptyList());
            return;
        }

        // 1. 构建节点列表 (GraphNodeVo)
        List<GraphNodeVo> nodes = path.getNodes().stream()
                .map(node -> toGraphNodeVo(node, progressMap))
                .collect(Collectors.toList());
        vo.setNodes(nodes);

        // 2. 构建边列表 (GraphEdgeVo)
        List<GraphEdgeVo> edges = path.getNodes().stream()
                .flatMap(node -> node.getPrerequisites() != null ? node
                        .getPrerequisites()
                        .stream() : Stream.empty())
                .map(this::toGraphEdgeVo)
                .collect(Collectors.toList());
        vo.setEdges(edges);
    }

    /**
     * 辅助方法：将 LearningPathNode 转换为 GraphNodeVo。
     * 它会从 context 中的 progressMap 获取节点的学习状态。
     */
    default GraphNodeVo toGraphNodeVo(LearningPathNode node, Map<Long, LearningStatus> progressMap) {
        if (node == null) {
            return null;
        }
        return GraphNodeVo.builder()
                .id(node.getId())
                .title(node.getNodeTitle())
                .status(progressMap.getOrDefault(node.getId(), LearningStatus.NOT_STARTED))
                .build();
    }

    /**
     * 辅助方法：将 PathNodeDependency 转换为 GraphEdgeVo。
     */
    default GraphEdgeVo toGraphEdgeVo(PathNodeDependency dependency) {
        if (dependency == null || dependency.getPrerequisiteNode() == null || dependency.getDependentNode() == null) {
            return null;
        }
        return GraphEdgeVo.builder()
                .source(dependency.getPrerequisiteNode().getId()) // 边的起点是前置节点
                .target(dependency.getDependentNode().getId())   // 边的终点是依赖节点
                .type(dependency.getDependencyType())
                .build();
    }
}