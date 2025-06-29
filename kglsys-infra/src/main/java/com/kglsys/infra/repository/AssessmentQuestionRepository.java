package com.kglsys.infra.repository;

import com.kglsys.domain.entity.AssessmentQuestion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * AssessmentQuestion 实体的 JPA Repository。
 */
@Repository
public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, Integer> {

    /**
     * 查找所有已启用的问题，并按顺序排序。
     * 【性能优化】: 使用 @EntityGraph(attributePaths = "options") 来解决 N+1 问题。
     * 当调用此方法时，JPA 会生成一条 JOIN SQL 语句，一次性将所有问题及其关联的选项全部查询出来，
     * 避免了在循环遍历问题时为每个问题单独查询其选项。
     * @return 包含完整选项信息的问题列表。
     */
    @EntityGraph(attributePaths = "options")
    List<AssessmentQuestion> findByIsActiveTrueOrderBySequenceAsc();
}