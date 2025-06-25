package com.kglsys.domain.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode
@Embeddable
public class RolePermissionKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 5937841857953229828L;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "permission_id")
    private Integer permissionId;

    // 必须提供无参构造函数、hashCode() 和 equals() 方法
}