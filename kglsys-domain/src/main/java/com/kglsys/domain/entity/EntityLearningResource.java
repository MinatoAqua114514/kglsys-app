package com.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * 实体与学习资源关联实体 (entity_learning_resources)
 */
@Entity
@Table(name = "entity_learning_resources", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"entity_id", "resource_id"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityLearningResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entity_id", nullable = false)
    private KgEntity entity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource_id", nullable = false)
    private LearningResource resource;

    @Column(name = "relevance_score", precision = 3, scale = 2)
    private BigDecimal relevanceScore;

    @Column(name = "is_primary")
    private Boolean isPrimary;
}