package com.kglsys.infra.repository;

import com.kglsys.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}