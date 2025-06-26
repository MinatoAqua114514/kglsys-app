package com.kglsys.domain.entity.base;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 用户个人资料扩展实体
 * <p>
 * 此实体与 UserEntity 构成一对一关系。
 * UserProfileEntity 是关系的“拥有方”，负责维护外键 user_id。
 */
@Data
@Entity
@Table(name = "user_profiles")
public class UserProfileEntity {

    /**
     * 关联的用户ID，同时也是此表的主键。
     */
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    /**
     * 定义到 UserEntity 的一对一关系。
     * - fetch = FetchType.LAZY: 在拥有方，Lazy loading 是有效的。只有在显式调用 getUser() 时才会加载关联的 UserEntity。
     * - @MapsId: 表示此实体的主键（由 @Id 标记的 userId 字段）的值，来源于关联实体 UserEntity 的主键。
     * 这是一种高效的共享主键策略。
     * - @JoinColumn: 指定外键列的名称。
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    @ToString.Exclude // 推荐：在关联字段上排除，避免因循环引用导致 StackOverflowError
    @EqualsAndHashCode.Exclude // 推荐：理由同上
    private UserEntity user;

    /**
     * 真实姓名
     */
    @Column(name = "full_name", length = 100)
    private String fullName;

    /**
     * 头像URL
     */
    @Column(name = "avatar_url", length = 512)
    private String avatarUrl;

    /**
     * 联系电话
     */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    /**
     * 学号 (学生特有)
     */
    @Column(name = "student_id", length = 50)
    private String studentId;

    /**
     * 院系或部门
     */
    @Column(name = "department", length = 100)
    private String department;

    /**
     * 职称 (教师特有)
     */
    @Column(name = "title", length = 100)
    private String title;

    /**
     * 创建时间 (由MySQL自动填充)
     */
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 最后更新时间 (由MySQL自动填充)
     */
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}