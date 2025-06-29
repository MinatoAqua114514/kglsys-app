package com.kglsys.infra.repository;

import com.kglsys.domain.entity.UserLearningProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * UserLearningProgress 实体的 JPA Repository。
 */
@Repository
public interface UserLearningProgressRepository extends JpaRepository<UserLearningProgress, Long> {

    /**
     * 根据用户ID和节点ID查找唯一的学习进度记录。
     * @param userId 用户ID
     * @param pathNodeId 学习路径节点ID
     * @return 学习进度 Optional
     */
    Optional<UserLearningProgress> findByUser_IdAndPathNode_Id(Long userId, Long pathNodeId);

    /**
     * 【性能优化】: 一次性获取一个用户在多个指定节点上的学习状态。
     * 这在加载整个学习路径图并需要展示每个节点状态时非常有用。
     * @param userId 用户ID
     * @param pathNodeIds 一组学习路径节点的ID
     * @return 匹配的学习进度列表
     */
    List<UserLearningProgress> findByUser_IdAndPathNode_IdIn(Long userId, Set<Long> pathNodeIds);
}