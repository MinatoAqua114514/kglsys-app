package com.kglsys.infra.repository;

import com.kglsys.domain.entity.PathNodeEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PathNodeEntityRepository extends JpaRepository<PathNodeEntity, Long> {
    List<PathNodeEntity> findByPathNodeId(Long pathNodeId);
    List<PathNodeEntity> findByEntityId(Long entityId);

    /**
     * 【新增】根据节点ID查找所有关联，并深度加载其关联的知识实体、
     * 以及知识实体关联的学习资源和题目。
     * @param pathNodeId 学习路径节点的ID
     * @return 深度加载的 PathNodeEntity 列表
     */
    @EntityGraph(attributePaths = {
            "entity",
            "entity.learningResources.resource",
            "entity.problemAssociations.problem"
    })
    List<PathNodeEntity> findByPathNode_Id(Long pathNodeId);
}