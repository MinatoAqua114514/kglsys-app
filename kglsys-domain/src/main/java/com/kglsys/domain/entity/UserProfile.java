package com.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
public class UserProfile {

    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100)
    private String fullName;

    @Column(length = 512)
    private String avatarUrl;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 50)
    private String studentId; // 学生特有

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String title; // 教师特有

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}