package com.kglsys.infra.repository.user;

import com.kglsys.domain.user.UserLearningProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLearningProfileRepository extends JpaRepository<UserLearningProfile, Long> {
    /**
     * 【新增方法】根据用户ID查找档案，并立即加载关联的 LearningStyle。
     * @param userId 用户ID
     * @return 包含 LearningStyle 的 UserLearningProfile
     */
    @EntityGraph(attributePaths = {"learningStyle"})
    Optional<UserLearningProfile> findWithStyleByUserId(Long userId);

    /**
     * 【新增】根据用户ID查找档案，并立即加载关联的 LearningPath。
     * @param userId 用户ID
     * @return 包含 LearningPath 的 UserLearningProfile
     */
    @EntityGraph(attributePaths = {"learningPath"})
    Optional<UserLearningProfile> findWithLearningPathByUserId(Long userId);
}