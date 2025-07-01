package com.kglsys.infra.repository.user;

import com.kglsys.domain.user.LearningStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LearningStyleRepository extends JpaRepository<LearningStyle, Integer> {

}