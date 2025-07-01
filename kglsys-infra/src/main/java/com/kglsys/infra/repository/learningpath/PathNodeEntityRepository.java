package com.kglsys.infra.repository.learningpath;

import com.kglsys.domain.learningpath.PathNodeEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PathNodeEntityRepository extends JpaRepository<PathNodeEntity, Long> {
    List<PathNodeEntity> findByEntityId(Long entityId);

    /**
     * 【新增】根据节点ID查找所有关联，并深度加载其关联的知识实体、
     * @param pathNodeId 学习路径节点的ID
     * @return 深度加载的 PathNodeEntity 列表
     */
    @EntityGraph(attributePaths = {"entity"})
    List<PathNodeEntity> findByPathNode_Id(Long pathNodeId);
}