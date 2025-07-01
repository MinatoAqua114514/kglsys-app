package com.kglsys.infra.repository.assessment;

import com.kglsys.domain.assessment.UserAssessmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * 用户测评答案记录的数据访问接口
 */
@Repository
public interface UserAssessmentAnswerRepository extends JpaRepository<UserAssessmentAnswer, Long> {}