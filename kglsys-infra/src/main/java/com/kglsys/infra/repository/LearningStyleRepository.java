package com.kglsys.infra.repository;

import com.kglsys.domain.entity.LearningStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningStyleRepository extends JpaRepository<LearningStyle, Integer> {}