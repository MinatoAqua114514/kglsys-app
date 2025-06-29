package com.kglsys.infra.repository;

import com.kglsys.domain.entity.PathNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PathNodeEntityRepository extends JpaRepository<PathNodeEntity, Long> {
    List<PathNodeEntity> findByPathNodeId(Long pathNodeId);
    List<PathNodeEntity> findByEntityId(Long entityId);
}