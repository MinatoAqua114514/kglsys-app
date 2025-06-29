package com.kglsys.infra.repository;

import com.kglsys.domain.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemEntityRepository extends JpaRepository<ProblemEntity, Long> {
    List<ProblemEntity> findByProblemId(Long problemId);
    List<ProblemEntity> findByEntityId(Long entityId);
}