package com.kglsys.domain.problem;

import com.kglsys.domain.common.enums.Difficulty;
import com.kglsys.domain.resource.LearningResource;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 编程题目实体
 */
@Entity
@Table(name = "problems")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @Column(name = "time_limit", nullable = false)
    private Integer timeLimit;

    @Column(name = "memory_limit", nullable = false)
    private Integer memoryLimit;

    @Column(name = "tags", columnDefinition = "json")
    private String tags; // JSON类型通常映射为String

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

    // 题目与知识实体的关联 (通过中间表)
    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProblemEntity> entityAssociations;

    // 一个题目可以有多个学习资源
    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    private Set<LearningResource> learningResources;
}