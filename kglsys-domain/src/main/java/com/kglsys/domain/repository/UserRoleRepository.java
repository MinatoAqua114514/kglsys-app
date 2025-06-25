package com.kglsys.domain.repository;

import com.kglsys.domain.entity.UserRoleEntity;
import com.kglsys.domain.entity.key.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleKey> { }
