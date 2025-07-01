package com.kglsys.application.mapper.knowledgegraph;

import com.kglsys.domain.knowledgegraph.KgEntity;
import com.kglsys.domain.knowledgegraph.KgEntityProperty;
import com.kglsys.domain.knowledgegraph.KgRelationship;
import com.kglsys.domain.problem.ProblemEntity;
import com.kglsys.domain.resource.EntityLearningResource;
import com.kglsys.dto.knowledgegraph.response.*;
import com.kglsys.dto.problem.response.ProblemSummaryVo;
import com.kglsys.dto.resource.response.LearningResourceVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface KnowledgeGraphMapper {

    // --- Mappings for STU-01 (Node Knowledge Graph) ---
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    KgGraphNodeVo toGraphNodeVo(KgEntity entity);

    List<KgGraphNodeVo> toGraphNodeVoList(List<KgEntity> entities);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "sourceEntity.id", target = "source")
    @Mapping(source = "targetEntity.id", target = "target")
    @Mapping(source = "relationType", target = "label")
    KgGraphEdgeVo toGraphEdgeVo(KgRelationship relationship);

    List<KgGraphEdgeVo> toGraphEdgeVoList(List<KgRelationship> relationships);


    // --- Mappings for STU-02 (Entity Detail) ---
    KgEntityPropertyVo toPropertyVo(KgEntityProperty property);

    @Mapping(source = "resource.id", target = "id")
    @Mapping(source = "resource.title", target = "title")
    @Mapping(source = "resource.resourceType", target = "resourceType")
    @Mapping(source = "resource.difficultyLevel", target = "difficultyLevel")
    @Mapping(source = "resource.contentUrl", target = "contentUrl")
    LearningResourceVo toResourceVo(EntityLearningResource entityLearningResource);

    @Mapping(source = "problem.id", target = "id")
    @Mapping(source = "problem.title", target = "title")
    @Mapping(source = "problem.difficulty", target = "difficulty")
    ProblemSummaryVo toProblemVo(ProblemEntity problemEntity);

    /**
     * 【核心修正】这是用于将 KgEntity 转换为 KgEntityDetailVo 的主映射方法。
     */
    @Mapping(target = "properties", source = "properties") // 显式映射 properties
    @Mapping(target = "resources", source = "learningResources") // 【修正1】显式映射 resources
    @Mapping(target = "problems", source = "problemAssociations") // 【修正1】显式映射 problems
    @Mapping(target = "outgoingRelationships", source = "sourceOfRelationships", qualifiedByName = "toOutgoingRelationshipVo")
    @Mapping(target = "incomingRelationships", source = "targetOfRelationships", qualifiedByName = "toIncomingRelationshipVo")
    KgEntityDetailVo toDetailVo(KgEntity entity);

    /**
     * 【修正2】辅助方法，将 Set<KgRelationship> 转换为 List<KgRelationshipVo>
     * @param relationships 输入的 Set
     * @return 输出的 List
     */
    @Named("toOutgoingRelationshipVo")
    default List<KgRelationshipVo> mapOutgoing(Set<KgRelationship> relationships) {
        if (relationships == null) {
            return List.of();
        }
        return relationships.stream().map(rel -> KgRelationshipVo.builder()
                .relationshipId(rel.getId())
                .relationType(rel.getRelationType())
                .description(rel.getDescription())
                .relatedEntityId(rel.getTargetEntity().getId())
                .relatedEntityName(rel.getTargetEntity().getName())
                .build()
        ).collect(Collectors.toList());
    }

    /**
     * 【修正2】辅助方法，将 Set<KgRelationship> 转换为 List<KgRelationshipVo>
     * @param relationships 输入的 Set
     * @return 输出的 List
     */
    @Named("toIncomingRelationshipVo")
    default List<KgRelationshipVo> mapIncoming(Set<KgRelationship> relationships) {
        if (relationships == null) {
            return List.of();
        }
        return relationships.stream().map(rel -> KgRelationshipVo.builder()
                .relationshipId(rel.getId())
                .relationType(rel.getRelationType())
                .description(rel.getDescription())
                .relatedEntityId(rel.getSourceEntity().getId())
                .relatedEntityName(rel.getSourceEntity().getName())
                .build()
        ).collect(Collectors.toList());
    }
}