package com.kglsys.infra.repository;

import com.kglsys.domain.entity.LearningPath;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * LearningPath 实体的 JPA Repository。
 * 【复杂查询】: 继承 JpaSpecificationExecutor<LearningPath> 接口，
 * 这使得我们可以使用 Criteria API 来构建动态的、类型安全的查询。
 */
@Repository
public interface LearningPathRepository extends JpaRepository<LearningPath, Long>, JpaSpecificationExecutor<LearningPath> {

    /**
     * 【修正】: 查找特定岗位推荐的学习路径。
     * 方法名从 findByLearningStyleId 修正为 findByForStyle_Id，以匹配 LearningPath 实体中的
     * 'forStyle' 字段及其主键 'id'。Spring Data JPA 会自动解析此方法名。
     * @param styleId 岗位 (learning_styles) 的ID
     * @return 学习路径列表
     */
    List<LearningPath> findByForStyle_Id(Integer styleId);

    /**
     * 【性能优化】: 查找并一次性加载路径下的所有节点。
     * @param id 学习路径的ID
     * @return 包含完整节点信息的学习路径
     */
    @EntityGraph(attributePaths = {"nodes"})
    Optional<LearningPath> findWithNodesById(Long id);
}