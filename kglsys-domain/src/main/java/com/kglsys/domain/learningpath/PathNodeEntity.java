package com.kglsys.domain.learningpath;

import com.kglsys.domain.knowledgegraph.KgEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 路径节点与实体关联表 (path_node_entities)
 */
@Entity
@Table(name = "path_node_entities", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"path_node_id", "entity_id"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PathNodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "path_node_id", nullable = false)
    private LearningPathNode pathNode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entity_id", nullable = false)
    private KgEntity entity;

    @Column(name = "is_core")
    private Boolean isCore;

    @Lob
    @Column(name = "learning_objective")
    private String learningObjective;
}