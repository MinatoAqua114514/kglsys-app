package com.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 学习路径节点实体
 */
@Entity
@Table(name = "learning_path_nodes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningPathNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "path_id", nullable = false)
    private LearningPath learningPath;

    @Column(name = "node_title", nullable = false)
    private String nodeTitle;

    @Lob
    @Column(name = "node_description")
    private String nodeDescription;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "estimated_hours")
    private Integer estimatedHours;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // 节点与知识实体的关联 (通过中间表)
    @OneToMany(mappedBy = "pathNode", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PathNodeEntity> pathNodeEntities;

    // 该节点作为前置节点的依赖关系
    @OneToMany(mappedBy = "prerequisiteNode", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PathNodeDependency> dependencies;

    // 该节点作为依赖节点的依赖关系
    @OneToMany(mappedBy = "dependentNode", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PathNodeDependency> prerequisites;
}