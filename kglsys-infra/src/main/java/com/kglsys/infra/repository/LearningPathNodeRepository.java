package com.kglsys.infra.repository;

import com.kglsys.domain.entity.LearningPathNode;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LearningPathNodeRepository extends JpaRepository<LearningPathNode, Long> {
    @EntityGraph(attributePaths = {"prerequisites.prerequisiteNode", "dependencies.dependentNode"})
    Optional<LearningPathNode> findWithDependenciesById(Long id);
}
