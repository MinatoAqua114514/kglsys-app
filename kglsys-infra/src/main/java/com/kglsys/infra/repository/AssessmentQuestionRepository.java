package com.kglsys.infra.repository;

import com.kglsys.domain.entity.AssessmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AssessmentQuestion实体的JPA仓库
 * JpaRepository<实体类, 主键类型>
 */
@Repository
public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, Integer> {
    List<AssessmentQuestion> findByIsActiveTrueOrderBySequenceAsc();
}