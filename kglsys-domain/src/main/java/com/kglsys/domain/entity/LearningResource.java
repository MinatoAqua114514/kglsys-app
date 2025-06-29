package com.kglsys.domain.entity;

import com.kglsys.domain.enums.DifficultyLevel;
import com.kglsys.domain.enums.ResourceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 学习资源实体
 */
@Entity
@Table(name = "learning_resources")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private ResourceType resourceType;

    @Column(name = "content_url", length = 512)
    private String contentUrl;

    @Lob
    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

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

    // 资源与知识实体的关联 (通过中间表)
    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<EntityLearningResource> entityAssociations;
}