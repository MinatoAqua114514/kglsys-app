package com.kglsys.domain.repository;

import com.kglsys.domain.entity.RolePermissionEntity;
import com.kglsys.domain.entity.key.RolePermissionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, RolePermissionKey> {}