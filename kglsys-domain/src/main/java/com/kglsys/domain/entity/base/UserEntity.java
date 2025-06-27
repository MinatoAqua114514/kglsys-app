package com.kglsys.domain.entity.base;

import com.kglsys.domain.entity.learning.UserLearningProfileEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id; // BIGINT UNSIGNED 对应 Long，正确

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    // --- 优化: BOOLEAN 映射为 boolean ---
    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired = true;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = true;

    // --- 优化: 时间类型和列名映射 ---
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // --- 关系映射: User 与 Role (多对多) ---
    @ManyToMany(fetch = FetchType.EAGER) // EAGER 可以在加载用户时立即获取角色
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    /**
     * 一对一关系：用户对应一个学习档案
     * mappedBy = "user" 表示关联关系由 UserLearningProfileEntity 的 'user' 字段维护
     * CascadeType.ALL: 对用户的操作（持久化、删除等）会级联到其学习档案
     * orphanRemoval = true: 如果将 userLearningProfile 从 user 中移除（user.setUserLearningProfile(null)），则对应的学习档案记录将被删除
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserLearningProfileEntity userLearningProfile;

    public Set<PermissionEntity> getAllPermissions() {
        return this.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .collect(Collectors.toSet());
    }
}