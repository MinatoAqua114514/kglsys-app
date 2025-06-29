package com.kglsys.application.mapper;

import com.kglsys.domain.entity.*;
import com.kglsys.domain.enums.LearningStatus;
import com.kglsys.dto.response.*;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface NodeDetailMapper {

    @Mapping(source = "node.id", target = "id")
    @Mapping(source = "node.nodeTitle", target = "title")
    @Mapping(source = "node.nodeDescription", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "neighbors", expression = "java(mapNeighbors(node))")
    @Mapping(target = "coreConcepts", expression = "java(mapCoreConcepts(pathNodeEntities))")
    @Mapping(target = "resources", expression = "java(mapResources(pathNodeEntities))")
    @Mapping(target = "problems", expression = "java(mapProblems(pathNodeEntities))")
    NodeDetailVo toDetailVo(LearningPathNode node, LearningStatus status, List<PathNodeEntity> pathNodeEntities);

    default List<NeighborNodeVo> mapNeighbors(LearningPathNode node) {
        Stream<NeighborNodeVo> prerequisites = node.getPrerequisites().stream()
                .map(dep -> NeighborNodeVo.builder()
                        .nodeId(dep.getPrerequisiteNode().getId())
                        .title(dep.getPrerequisiteNode().getNodeTitle())
                        .type(NeighborNodeVo.NeighborType.PREREQUISITE)
                        .build());

        Stream<NeighborNodeVo> dependents = node.getDependencies().stream()
                .map(dep -> NeighborNodeVo.builder()
                        .nodeId(dep.getDependentNode().getId())
                        .title(dep.getDependentNode().getNodeTitle())
                        .type(NeighborNodeVo.NeighborType.DEPENDENT)
                        .build());

        return Stream.concat(prerequisites, dependents).collect(Collectors.toList());
    }

    default List<KgEntitySummaryVo> mapCoreConcepts(List<PathNodeEntity> pathNodeEntities) {
        return pathNodeEntities.stream()
                .map(PathNodeEntity::getEntity)
                .distinct()
                .map(this::toKgEntitySummaryVo)
                .collect(Collectors.toList());
    }

    default List<LearningResourceVo> mapResources(List<PathNodeEntity> pathNodeEntities) {
        return pathNodeEntities.stream()
                .flatMap(pne -> pne.getEntity().getLearningResources().stream())
                .map(EntityLearningResource::getResource)
                .distinct()
                .map(this::toLearningResourceVo)
                .collect(Collectors.toList());
    }

    default List<ProblemSummaryVo> mapProblems(List<PathNodeEntity> pathNodeEntities) {
        return pathNodeEntities.stream()
                .flatMap(pne -> pne.getEntity().getProblemAssociations().stream())
                .map(ProblemEntity::getProblem)
                .distinct()
                .map(this::toProblemSummaryVo)
                .collect(Collectors.toList());
    }

    // --- Helper Mappers ---
    KgEntitySummaryVo toKgEntitySummaryVo(KgEntity entity);
    LearningResourceVo toLearningResourceVo(LearningResource resource);
    ProblemSummaryVo toProblemSummaryVo(Problem problem);
}