package com.kglsys.domain.repository.base;

import com.kglsys.domain.entity.base.RolePermissionEntity;
import com.kglsys.domain.entity.base.key.RolePermissionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, RolePermissionKey> {}