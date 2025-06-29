package com.kglsys.domain.entity;

import com.kglsys.domain.enums.TestDepth;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 题目与实体关联实体 (problem_entities)
 */
@Entity
@Table(name = "problem_entities", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"problem_id", "entity_id"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entity_id", nullable = false)
    private KgEntity entity;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_depth", nullable = false)
    private TestDepth testDepth;
}