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
public class UserRoleKey implements Serializable {

    @Serial
    private static final long serialVersionUID = -8295221958898539634L;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Integer roleId;

    // 必须提供无参构造函数、hashCode() 和 equals() 方法
}