package com.kglsys.domain.entity.base;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 操作权限定义实体
 */
@Data
@Entity
@Table(name = "permissions")
public class PermissionEntity {

    /**
     * 权限唯一ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    /**
     * 权限名称 (英文标识, 如: profile:edit, user:create)
     */
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    /**
     * 权限显示名称 (中文，如: 编辑个人资料, 创建用户)
     */
    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    /**
     * 权限描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 创建时间 (由MySQL自动填充)
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // --- Add this relationship ---
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RolePermissionEntity> rolePermissions;
}