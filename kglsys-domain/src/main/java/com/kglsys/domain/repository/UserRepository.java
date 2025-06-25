package com.kglsys.domain.repository;

import com.kglsys.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户仓库接口
 * 继承 JpaRepository<实体类型, 主键类型>
 * 推荐同时继承 JpaSpecificationExecutor 以支持动态条件查询
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    // --- 派生查询 (Derived Query Methods) ---
    // Spring Data JPA 会根据方法名自动创建查询。非常直观。

    /**
     * 根据用户名精确查找用户 (忽略大小写)。
     * findBy + 实体属性名 + [IgnoreCase]
     * @param username 用户名
     * @return Optional<UserEntity>
     */
    Optional<UserEntity> findByUsernameIgnoreCase(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}