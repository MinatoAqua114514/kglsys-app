package com.kglsys.domain.entity;

import com.kglsys.domain.enums.LearningStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 用户节点学习进度实体，映射到 `user_learning_progress` 表。
 */
@Entity
@Table(name = "user_learning_progress", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "path_node_id"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLearningProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的用户。
     * 不再是原始的 Long userId，而是直接的实体关联，这更符合JPA的设计，
     * 提供了更好的类型安全和导航能力。FetchType.LAZY 确保了不会造成性能问题。
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "path_node_id", nullable = false)
    private LearningPathNode pathNode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LearningStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}