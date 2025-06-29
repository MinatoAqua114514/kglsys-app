package com.kglsys.infra.repository;

import com.kglsys.domain.entity.UserLearningProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserLearningProgressRepository extends JpaRepository<UserLearningProgress, Long> {

    Optional<UserLearningProgress> findByUserIdAndPathNodeId(Long userId, Long pathNodeId);

    // 优化的查询：一次性获取一个用户在多个节点上的学习状态
    List<UserLearningProgress> findByUserIdAndPathNodeIdIn(Long userId, Set<Long> pathNodeIds);
}