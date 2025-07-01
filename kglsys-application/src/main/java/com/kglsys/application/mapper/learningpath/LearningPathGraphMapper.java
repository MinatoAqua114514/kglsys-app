package com.kglsys.application.mapper.learningpath;

import com.kglsys.domain.learningpath.LearningPath;
import com.kglsys.domain.learningpath.LearningPathNode;
import com.kglsys.domain.learningpath.PathNodeDependency;
import com.kglsys.domain.learningpath.enums.LearningStatus;
import com.kglsys.dto.knowledgegraph.response.GraphEdgeVo;
import com.kglsys.dto.knowledgegraph.response.GraphNodeVo;
import com.kglsys.dto.learningpath.response.LearningPathGraphVo;
import org.mapstruct.Context;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

@Mapper(componentModel = "spring")
@DecoratedWith(LearningPathGraphMapperDecorator.class)
@Qualifier("delegate")
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
                .source(dependency.getPrerequisiteNode().getId())
                .target(dependency.getDependentNode().getId())
                .type(dependency.getDependencyType())
                .build();
    }
}