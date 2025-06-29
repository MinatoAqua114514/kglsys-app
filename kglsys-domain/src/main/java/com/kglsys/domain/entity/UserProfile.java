package com.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 用户个人资料扩展实体，映射到 `user_profiles` 表。
 * 使用共享主键策略与 User 实体进行一对一关联。
 */
@Entity
@Table(name = "user_profiles")
@Getter
@Setter
public class UserProfile {

    /**
     * 主键，与 User 的主键共享。
     */
    @Id
    private Long userId;

    /**
     * 指向 User 的一对一关联。
     * @MapsId: 表示该实体的主键值由 'user' 这个关联对象的主键提供。
     * @JoinColumn: 定义了外键列。
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    // ... 其他字段保持不变 ...
    @Column(length = 100)
    private String fullName;

    @Column(length = 512)
    private String avatarUrl;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 50)
    private String studentId;

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String title;

    @Column(name = "created_at", updatable = false, nullable = false)
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
        this.updatedAt = LocalDateTime.now();
    }
}