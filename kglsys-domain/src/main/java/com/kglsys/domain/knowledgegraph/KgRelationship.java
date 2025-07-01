package com.kglsys.domain.knowledgegraph;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 知识图谱关系实体
 */
@Entity
@Table(name = "kg_relationships")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KgRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "relationship_id", nullable = false, unique = true, length = 100)
    private String relationshipId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_entity_id", nullable = false)
    private KgEntity sourceEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_entity_id", nullable = false)
    private KgEntity targetEntity;

    @Column(name = "relation_type", nullable = false, length = 100)
    private String relationType;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "strength", length = 50)
    private String strength;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "relationship", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<KgRelationshipProperty> properties;
}