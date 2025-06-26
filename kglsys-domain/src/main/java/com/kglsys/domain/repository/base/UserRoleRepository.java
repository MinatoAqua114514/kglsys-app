package com.kglsys.domain.repository.base;

import com.kglsys.domain.entity.base.UserRoleEntity;
import com.kglsys.domain.entity.base.key.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleKey> { }
