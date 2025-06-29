package com.kglsys.domain.entity;

import com.kglsys.domain.enums.LearningStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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

    @Column(name = "user_id", nullable = false)
    private Long userId; // 这里我们只存ID，不建立JPA的ManyToOne关系，避免循环依赖和不必要的加载

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_node_id", nullable = false)
    private LearningPathNode pathNode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
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