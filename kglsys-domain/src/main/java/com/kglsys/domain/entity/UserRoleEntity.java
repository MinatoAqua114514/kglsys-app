package com.kglsys.domain.entity;

import com.kglsys.domain.entity.key.UserRoleKey;
import jakarta.persistence.*;
import lombok.Data;

/**
 * 用户角色关联实体
 */
@Data
@Entity
@Table(name = "user_roles")
public class UserRoleEntity {

    @EmbeddedId
    private UserRoleKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // 映射到复合主键中的 userId 属性
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId") // 映射到复合主键中的 roleId 属性
    @JoinColumn(name = "role_id")
    private RoleEntity role;
}