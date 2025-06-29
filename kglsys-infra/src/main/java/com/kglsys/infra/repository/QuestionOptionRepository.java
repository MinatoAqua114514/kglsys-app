package com.kglsys.infra.repository;

import com.kglsys.domain.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Integer> {}