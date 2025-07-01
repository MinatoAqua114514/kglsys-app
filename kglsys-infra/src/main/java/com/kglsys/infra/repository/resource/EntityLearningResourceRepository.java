package com.kglsys.infra.repository.resource;

import com.kglsys.domain.resource.EntityLearningResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntityLearningResourceRepository extends JpaRepository<EntityLearningResource, Long> {
    List<EntityLearningResource> findByEntityId(Long entityId);
    List<EntityLearningResource> findByResourceId(Long resourceId);
}