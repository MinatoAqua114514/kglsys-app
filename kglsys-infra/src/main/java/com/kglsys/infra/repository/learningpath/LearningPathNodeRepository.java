package com.kglsys.infra.repository.learningpath;

import com.kglsys.domain.learningpath.LearningPathNode;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * LearningPathNode 实体的 JPA Repository。
 */
@Repository
public interface LearningPathNodeRepository extends JpaRepository<LearningPathNode, Long> {

    /**
     * 【性能优化】: 根据ID查找节点，并同时加载其所有前置和后置依赖节点。
     * 'prerequisites.prerequisiteNode' 表示加载 prerequisites 集合，并对集合中的每个元素，再加载其 prerequisiteNode 字段。
     * 这对于构建学习路径图谱至关重要，可以一次性获取一个节点及其所有直接相邻节点。
     * @param id 节点ID
     * @return 包含完整依赖信息的节点 Optional
     */
    @EntityGraph(attributePaths = {"prerequisites.prerequisiteNode", "dependencies.dependentNode"})
    Optional<LearningPathNode> findWithDependenciesById(Long id);
}