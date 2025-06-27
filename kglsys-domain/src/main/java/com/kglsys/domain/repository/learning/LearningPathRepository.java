package com.kglsys.domain.repository.learning;

import com.kglsys.domain.entity.learning.LearningPathEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPathRepository extends JpaRepository<LearningPathEntity, Long> {

    /**
     * 根据学习风格ID查找所有相关的学习路径
     * @param styleId 学习风格的ID
     * @return 匹配的学习路径列表
     */
    List<LearningPathEntity> findByForStyle_Id(Integer styleId);
}