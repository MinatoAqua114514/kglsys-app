package com.kglsys.domain.repository.graph;

import com.kglsys.domain.entity.graph.KnowledgeGraphEdgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface KnowledgeGraphEdgeRepository extends JpaRepository<KnowledgeGraphEdgeEntity, Long> {

    /**
     * 根据一组节点ID，查询所有在这些节点之间存在的边。
     * 这是一个非常关键的查询，它确保我们只返回与当前学习路径相关的边，而不是图谱中所有的边。
     * @param nodeIds 学习路径中包含的所有知识节点的ID集合
     * @return 存在于这些节点之间的边的实体列表
     */
    @Query("SELECT edge FROM KnowledgeGraphEdgeEntity edge " +
            "WHERE edge.sourceNode.id IN :nodeIds AND edge.targetNode.id IN :nodeIds")
    List<KnowledgeGraphEdgeEntity> findEdgesConnectingNodes(@Param("nodeIds") Set<Long> nodeIds);

    /**
     * 根据一组起始节点ID，查询所有从这些节点出发的边。
     * 这是新的查询方法，它会找到所有相关的出边，无论目标节点在不在这个集合里。
     * Spring Data JPA会根据方法名自动生成查询。
     *
     * @param sourceNodeIds 学习路径中包含的所有知识节点的ID集合
     * @return 从这些节点出发的所有边的实体列表
     */
    List<KnowledgeGraphEdgeEntity> findBySourceNodeIdIn(Set<Long> sourceNodeIds);
}