package com.kglsys.domain.entity.base;

import com.kglsys.domain.entity.base.key.RolePermissionKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色权限关联实体
 */
@Getter
@Setter
@Entity
@Table(name = "role_permissions")
public class RolePermissionEntity {

    @EmbeddedId
    private RolePermissionKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    private PermissionEntity permission;
}