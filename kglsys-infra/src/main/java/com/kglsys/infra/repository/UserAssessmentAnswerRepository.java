package com.kglsys.infra.repository;

import com.kglsys.domain.entity.UserAssessmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户测评答案记录的数据访问接口
 */
@Repository
public interface UserAssessmentAnswerRepository extends JpaRepository<UserAssessmentAnswer, Long> {}