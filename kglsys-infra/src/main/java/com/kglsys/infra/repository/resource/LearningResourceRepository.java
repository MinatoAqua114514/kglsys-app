package com.kglsys.infra.repository.resource;

import com.kglsys.domain.resource.LearningResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningResourceRepository extends JpaRepository<LearningResource, Long> {
}
