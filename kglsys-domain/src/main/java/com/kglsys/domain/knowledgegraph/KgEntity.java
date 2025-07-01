package com.kglsys.domain.knowledgegraph;

import com.kglsys.domain.resource.EntityLearningResource;
import com.kglsys.domain.learningpath.PathNodeEntity;
import com.kglsys.domain.problem.ProblemEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 知识图谱实体
 */
@Entity
@Table(name = "kg_entities", indexes = {
        @Index(name = "idx_entity_id", columnList = "entity_id", unique = true),
        @Index(name = "idx_type", columnList = "type")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KgEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_id", nullable = false, unique = true, length = 100)
    private String entityId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 关系映射
    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<KgEntityProperty> properties;

    @OneToMany(mappedBy = "sourceEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<KgRelationship> sourceOfRelationships;

    @OneToMany(mappedBy = "targetEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<KgRelationship> targetOfRelationships;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<EntityLearningResource> learningResources;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProblemEntity> problemAssociations;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PathNodeEntity> pathNodeAssociations;
}