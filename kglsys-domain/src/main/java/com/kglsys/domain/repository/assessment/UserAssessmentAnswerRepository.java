package com.kglsys.domain.repository.assessment;

import com.kglsys.domain.entity.assessment.UserAssessmentAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户测评答案记录的数据访问接口
 */
@Repository
public interface UserAssessmentAnswerRepository extends JpaRepository<UserAssessmentAnswerEntity, Long> {

    /**
     * 根据用户ID和问题ID查找用户的答题记录。
     * Spring Data JPA 会自动解析方法名生成查询：
     * "SELECT uaa FROM UserAssessmentAnswerEntity uaa WHERE uaa.user.id = :userId AND uaa.question.id = :questionId"
     *
     * @param userId     用户ID
     * @param questionId 问题ID
     * @return 返回一个包含用户答案实体的 Optional，如果不存在则为空。
     */
    Optional<UserAssessmentAnswerEntity> findByUser_IdAndQuestion_Id(Long userId, Integer questionId);

    /**
     * 新增方法：根据用户ID查找该用户所有的答题记录
     * @param userId 用户ID
     * @return 该用户的所有答案实体列表
     */
    List<UserAssessmentAnswerEntity> findAllByUser_Id(Long userId);
}