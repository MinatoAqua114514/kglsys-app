package com.kglsys.domain.repository.base;

import com.kglsys.domain.entity.base.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity,Long> {
}
