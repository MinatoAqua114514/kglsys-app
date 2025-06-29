package com.kglsys.domain.entity;

import com.kglsys.domain.enums.DependencyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 路径节点依赖关系实体 (path_node_dependencies)
 */
@Entity
@Table(name = "path_node_dependencies", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"prerequisite_node_id", "dependent_node_id"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PathNodeDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prerequisite_node_id", nullable = false)
    private LearningPathNode prerequisiteNode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dependent_node_id", nullable = false)
    private LearningPathNode dependentNode;

    @Enumerated(EnumType.STRING)
    @Column(name = "dependency_type", nullable = false)
    private DependencyType dependencyType;
}