package com.kglsys.domain.repository.learning;

import com.kglsys.domain.entity.learning.UserLearningProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserLearningProfileRepository extends JpaRepository<UserLearningProfileEntity, Long> {
    /**
     * 更新指定用户的学习路径，前提是该用户的学习风格ID(learning_style_id)不为空。
     * 使用@Modifying注解表示这是一个更新或删除操作。
     * @return 返回受影响的行数。如果更新成功，返回1；如果用户不存在或没有学习风格，返回0。
     */
    @Modifying
    @Query("UPDATE UserLearningProfileEntity p SET p.assignedPath.id = :assignedPathId, " +
            "p.assessmentCompletedAt = :completedAt WHERE p.userId = :userId AND p.learningStyle IS NOT NULL")
    int updateAssignedPathIfStyleExists(@Param("userId") Long userId,
                                        @Param("assignedPathId") Long assignedPathId,
                                        @Param("completedAt") LocalDateTime completedAt);
}