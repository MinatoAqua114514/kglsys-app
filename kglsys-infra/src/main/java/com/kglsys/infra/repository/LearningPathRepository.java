package com.kglsys.infra.repository;

import com.kglsys.domain.entity.LearningPath;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningPathRepository extends JpaRepository<LearningPath, Long> {

    /**LearningPathRepository
     * 查找特定岗位推荐的学习路径。
     * @param styleId 岗位 (learning_styles) 的ID
     * @return 学习路径列表
     */
    List<LearningPath> findByLearningStyleId(Integer styleId);

    /**
     * 查找并一次性加载路径下的所有节点，以及每个节点关联的实体信息。
     * 这是一个深度加载的例子，避免了多层级的 N+1 问题。
     * @param id 学习路径的ID
     * @return 包含完整节点和实体信息的学习路径
     */
    @EntityGraph(attributePaths = {"nodes", "nodes.pathNodeEntities", "nodes.pathNodeEntities.entity"})
    Optional<LearningPath> findFullyLoadedById(Long id);
}