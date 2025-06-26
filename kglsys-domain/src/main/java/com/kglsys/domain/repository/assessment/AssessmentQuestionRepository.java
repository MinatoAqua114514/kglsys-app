package com.kglsys.domain.repository.assessment;

import com.kglsys.domain.entity.assessment.AssessmentQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * AssessmentQuestion实体的JPA仓库
 * JpaRepository<实体类, 主键类型>
 */
@Repository
public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestionEntity, Integer> {
    // Spring Data JPA 已经提供了 findById(Integer id) 方法。
    // 如果需要更复杂的查询，例如通过 'isActive' 状态查找，可以像下面这样定义：
    // Optional<AssessmentQuestion> findByIdAndIsActiveTrue(Integer id);
}
