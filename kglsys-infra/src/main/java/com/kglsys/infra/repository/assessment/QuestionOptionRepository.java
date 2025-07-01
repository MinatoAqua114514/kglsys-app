package com.kglsys.infra.repository.assessment;

import com.kglsys.domain.assessment.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Integer> {}